#!/usr/bin/env python3
"""补抓 Palantir Foundry API v2 中被主爬虫跳过的端点参考页。

原爬虫 palantir_docs_crawler.py 只提取 page.content.type == "markdown" 的页面，
而 API v2 的端点参考页是 content.type == "endpoint"（结构化 OpenAPI 数据：
path / 参数 / 请求响应体 / 错误码），因此被误判为"空页"跳过。本脚本：

  1. 从 API v2 侧边栏发现全部页面，找出本地缺失的 HTML
  2. 抓取这些页面，把 endpoint 结构转换为 Markdown（方法/路径、scopes、
     参数表、请求/响应体树、示例、错误码表）
  3. 渲染为 HTML（复用主爬虫的模板 / 图片下载 / 链接重写逻辑）
  4. 重新生成 palantir_docs/api/index.html 目录

用法示例:
    uv run palantir_endpoint_docs.py                 # 只补抓本地缺失的页面
    uv run palantir_endpoint_docs.py --list          # 只列出将要处理的页面
    uv run palantir_endpoint_docs.py --force         # 忽略本地已有，全部重抓
    uv run palantir_endpoint_docs.py --save-md       # 同时保存原始 Markdown
    uv run palantir_endpoint_docs.py --max-pages 10  # 限制页面数（调试用）
"""

from __future__ import annotations

import argparse
import html
import json
import logging
import re
import sys
from concurrent.futures import ThreadPoolExecutor, as_completed
from pathlib import Path

import markdown as md_lib

from palantir_docs_crawler import (
    API_SEED_URL,
    API_URL_PREFIX,
    HTML_TEMPLATE,
    INDEX_STYLE,
    AssetDownloader,
    DocClient,
    collect_sidebar_urls,
    get_page_props,
    rel_link,
    rewrite_markdown,
)

LOG = logging.getLogger("palantir-endpoint-fix")

BASE_URL = "https://www.palantir.com"


# --------------------------------------------------------------------------- #
# 页面发现与本地路径映射
# --------------------------------------------------------------------------- #
def discover_api_urls(client: DocClient) -> set[str]:
    """从 API v2 落地页侧边栏发现全部页面 URL。"""
    props = get_page_props(client, API_SEED_URL)
    urls = collect_sidebar_urls(props.get("sidebarNavItems"))
    return {u for u in urls if u.startswith(API_URL_PREFIX)}


def url_to_local(url: str) -> str | None:
    """站内文档 URL -> 本地 HTML 相对路径（与主爬虫的映射规则一致）。"""
    path = url.split("?")[0].split("#")[0]
    if path.startswith("/docs"):
        path = path.removeprefix("/docs")
    path = path.rstrip("/")
    if path.startswith("/foundry/api/"):
        return "api/" + path.removeprefix("/foundry/api/") + ".html"
    if path.startswith("/foundry/"):
        rest = path.removeprefix("/foundry/")
        if "/" in rest:
            return rest + ".html"
    return None


class LocalPageMap:
    """rewrite_markdown 所需的 rel_pages 映射：站内链接 -> 本地路径。

    目标页面在「本次计划写入集合」或磁盘上存在时才重写为本地路径，
    否则保持外链。
    """

    def __init__(self, out_root: Path, planned: set[str]):
        self.out_root = out_root
        self.planned = planned  # 本次会存在的本地 relpath 集合

    def get(self, url: str, default=None):
        local = url_to_local(url)
        if local and (local in self.planned or (self.out_root / local).exists()):
            return local
        return default


# --------------------------------------------------------------------------- #
# endpoint 结构 -> Markdown
# --------------------------------------------------------------------------- #
def clean_cell(text: str | None) -> str:
    """把描述文本压成单行、转义管道符，适配 Markdown 表格单元格。"""
    if not text:
        return ""
    return re.sub(r"\s*\n\s*", "<br>", text.strip()).replace("|", "\\|")


def type_label(type_obj: dict | None) -> str:
    """把类型结构转成可读标签，如 list<ScheduleRid>、string、object。"""
    if not isinstance(type_obj, dict):
        return ""
    tt = type_obj.get("type") or ""
    if tt == "listType":
        inner = type_obj.get("listType") or {}
        alias = inner.get("listItemAlias")
        base = type_label(inner.get("listItemType"))
        return f"list<{alias or base}>"
    if tt == "mapType":
        inner = type_obj.get("mapType") or {}
        value = type_label(inner.get("valueType"))
        return f"map<string, {value}>" if value else "map"
    return re.sub(r"Type$", "", tt)


def flatten_params(params: list | None, prefix: str = "") -> list[tuple[str, dict]]:
    """把参数树拍平为 (点号路径名, 参数节点) 列表（含顶层自身）。"""
    rows: list[tuple[str, dict]] = []
    for param in params or []:
        name = f"{prefix}{param.get('name') or ''}".strip()
        if name:
            rows.append((name, param))
        rows += flatten_params(param.get("children"), prefix=f"{name}.")
    return rows


def inline_example(example) -> str:
    """把示例值压成单行内联代码（换行折叠、管道符转义），适配表格单元格。"""
    text = re.sub(r"\s*\n\s*", " ", str(example)).strip()
    return f"`{text.replace('|', '\\|')}`"


def render_param_table(rows: list[tuple[str, dict]]) -> list[str]:
    lines = ["| 参数 | 类型 | 必填 | 说明 |", "| --- | --- | --- | --- |"]
    for name, param in rows:
        desc = clean_cell(param.get("description") or param.get("generalDescription"))
        example = param.get("example")
        if example:
            rendered = inline_example(example)
            desc = f"{desc}<br>示例: {rendered}" if desc else f"示例: {rendered}"
        required = "是" if param.get("required") else "否"
        lines.append(
            f"| `{name}` | {type_label(param.get('type')) or '—'} | {required} | {desc or '—'} |")
    return lines


def pretty_example(raw: str | None) -> tuple[str, str] | None:
    """返回 (语言, 格式化后的文本)；非 JSON 原样返回。"""
    if not raw or not raw.strip():
        return None
    try:
        return "json", json.dumps(json.loads(raw), indent=2, ensure_ascii=False)
    except (ValueError, TypeError):
        return "text", raw.strip()


def render_body_section(title: str, body: dict | None) -> list[str]:
    """渲染请求/响应体：顶层字段 + 子字段拍平为一张表，附示例代码块。"""
    if not body:
        return []
    lines = [f"## {title}", ""]
    name = body.get("name")
    if name:
        lines.append(f"**{name}**")
        desc = clean_cell(body.get("description") or body.get("generalDescription"))
        if desc:
            lines += ["", desc]
        lines.append("")
    rows = flatten_params([body])
    if rows:
        lines += render_param_table(rows) + [""]
    example = pretty_example(body.get("example"))
    if example:
        lines += [f"```{example[0]}", example[1], "```", ""]
    return lines


def endpoint_to_markdown(endpoint: dict) -> str:
    """把 endpoint 结构转换为 Markdown 文档。"""
    op = ((endpoint.get("operationType") or {}).get("type") or "").upper()
    lines: list[str] = [f"`{op} {endpoint.get('path') or ''}`", ""]

    desc = (endpoint.get("description") or "").strip()
    if desc:
        lines += [desc, ""]

    scopes = [s.get("name") for s in endpoint.get("scopes") or [] if s.get("name")]
    if scopes:
        lines += ["**OAuth2 scopes**: " + " ".join(f"`{s}`" for s in scopes), ""]

    if endpoint.get("pathParameters"):
        lines += ["## Path parameters", "",
                  *render_param_table(flatten_params(endpoint["pathParameters"])), ""]
    if endpoint.get("queryParameters"):
        lines += ["## Query parameters", "",
                  *render_param_table(flatten_params(endpoint["queryParameters"])), ""]

    lines += render_body_section("Request body", endpoint.get("request"))
    response = endpoint.get("response") or {}
    lines += render_body_section("Response", response.get("body") or response or None)

    errors = endpoint.get("errorResponses") or []
    if errors:
        lines += ["## Error responses", "",
                  "| 错误码 | 错误名称 | 说明 |", "| --- | --- | --- |"]
        for err in errors:
            lines.append(f"| {err.get('errorCode') or '—'} | `{err.get('name') or '—'}` "
                         f"| {clean_cell(err.get('description')) or '—'} |")
        lines.append("")

    return "\n".join(lines).strip() + "\n"


def extract_content(props: dict) -> tuple[str | None, str, str]:
    """返回 (markdown, title, 内容类型)；无法处理时 markdown 为 None。"""
    page = props.get("page") or {}
    content = page.get("content") or {}
    ctype = content.get("type") or ""
    title = ((props.get("metadata") or {}).get("title")
             or page.get("title") or "").strip()

    if ctype == "markdown":
        return content.get("markdown"), title, ctype
    if ctype == "endpoint":
        endpoint = content.get("endpoint") or {}
        if not endpoint:
            return None, title, ctype
        return endpoint_to_markdown(endpoint), title, ctype
    return None, title, ctype or "unknown"


# --------------------------------------------------------------------------- #
# 页面抓取
# --------------------------------------------------------------------------- #
def process_page(
    client: DocClient,
    url: str,
    out_root: Path,
    page_map: LocalPageMap,
    downloader: AssetDownloader,
    save_md: bool,
) -> tuple[str, str, str]:
    """抓取并写出一个页面，返回 (状态, 本地路径, 说明)。"""
    page_url = BASE_URL + "/docs" + url
    props = get_page_props(client, "/docs" + url)
    markdown_text, title, ctype = extract_content(props)
    if not markdown_text:
        return "skip", "", f"无法处理的页面类型: {ctype}"

    # 本地路径由端点 URL 推导（api/v2/...）
    local = url_to_local(url)
    if not local:
        return "skip", "", f"URL 无法映射到本地路径: {url}"

    text = rewrite_markdown(markdown_text, local, page_url, downloader, page_map)
    html_body = md_lib.markdown(text, extensions=["tables", "fenced_code", "sane_lists"])
    html_doc = HTML_TEMPLATE.format(
        title=html.escape(title or url),
        index_link=rel_link(local, "index.html"),
        content=html_body,
    )
    dest = out_root / local
    dest.parent.mkdir(parents=True, exist_ok=True)
    dest.write_text(html_doc, encoding="utf-8")
    if save_md:
        dest.with_suffix(".md").write_text(markdown_text, encoding="utf-8")
    return "ok", local, title


# --------------------------------------------------------------------------- #
# 目录索引
# --------------------------------------------------------------------------- #
TITLE_RE = re.compile(r"<title>(.*?)</title>", re.S)


def build_api_index(out_root: Path) -> int:
    """扫描 api/ 下全部 HTML，按资源分组重新生成 api/index.html。"""
    api_root = out_root / "api"
    pages: list[tuple[str, str, str]] = []  # (分组, 相对路径, 标题)
    for html_file in sorted(api_root.glob("**/*.html")):
        rel = html_file.relative_to(out_root).as_posix()
        if html_file.name == "index.html":
            continue
        match = TITLE_RE.search(html_file.read_text(encoding="utf-8")[:4000])
        title = html.unescape(match.group(1)).strip() if match else ""
        title = re.sub(r"\s*•\s*API Reference\s*$", "", title) or rel
        parts = html_file.parent.relative_to(api_root).parts
        group = parts[0] if parts else "(root)"
        pages.append((group, rel, title))

    groups: dict[str, list[tuple[str, str]]] = {}
    for group, rel, title in pages:
        groups.setdefault(group, []).append((rel, title))

    parts_out = ["<!DOCTYPE html><html lang='zh'><head><meta charset='utf-8'>",
                 "<title>Foundry API v2 · 文档目录</title>", INDEX_STYLE, "</head><body>",
                 "<h1>Foundry API v2</h1>",
                 "<p><a href='../index.html'>← 返回总目录</a> · "
                 f"共 {len(pages)} 个页面</p>"]
    for group in sorted(groups):
        entries = sorted(groups[group])
        parts_out.append(f"<h2>{html.escape(group)}</h2><ul>")
        for rel, title in entries:
            rel_within = rel.removeprefix("api/")
            parts_out.append(
                f"<li><a href='{html.escape(rel_within, quote=True)}'>"
                f"{html.escape(title)}</a></li>")
        parts_out.append("</ul>")
    parts_out.append("</body></html>")

    api_root.mkdir(parents=True, exist_ok=True)
    (api_root / "index.html").write_text("\n".join(parts_out), encoding="utf-8")
    return len(pages)


# --------------------------------------------------------------------------- #
# 主流程
# --------------------------------------------------------------------------- #
def parse_args(argv: list[str] | None = None) -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="补抓被跳过的 Foundry API v2 端点页")
    parser.add_argument("--output", default="palantir_docs", help="输出目录")
    parser.add_argument("--workers", type=int, default=6, help="并发线程数")
    parser.add_argument("--max-pages", type=int, default=0, help="限制页面数（0 = 不限制）")
    parser.add_argument("--force", action="store_true", help="忽略本地已有文件，全部重抓")
    parser.add_argument("--save-md", action="store_true", help="同时保存 Markdown 文件")
    parser.add_argument("--list", action="store_true", help="只列出待处理页面，不抓取")
    parser.add_argument("-v", "--verbose", action="store_true")
    return parser.parse_args(argv)


def main(argv: list[str] | None = None) -> int:
    args = parse_args(argv)
    logging.basicConfig(
        level=logging.DEBUG if args.verbose else logging.INFO,
        format="%(asctime)s %(levelname)s %(message)s",
        datefmt="%H:%M:%S",
    )
    out_root = Path(args.output)
    out_root.mkdir(parents=True, exist_ok=True)

    client = DocClient(workers=args.workers)
    LOG.info("发现 API v2 侧边栏页面…")
    api_urls = discover_api_urls(client)
    LOG.info("API v2 页面总数: %d", len(api_urls))

    if args.force:
        targets = sorted(api_urls)
    else:
        targets = sorted(u for u in api_urls if not (out_root / url_to_local(u)).exists())
    if args.max_pages > 0:
        targets = targets[: args.max_pages]
    LOG.info("待处理页面: %d", len(targets))

    if args.list:
        for url in targets:
            LOG.info("  %s", url)
        return 0
    if not targets:
        LOG.info("没有需要补抓的页面。")
        return 0

    planned = {url_to_local(u) for u in api_urls if url_to_local(u)}
    page_map = LocalPageMap(out_root, planned)
    downloader = AssetDownloader(client, out_root)

    ok: list[str] = []
    skipped: list[tuple[str, str]] = []
    failed: list[tuple[str, str]] = []

    with ThreadPoolExecutor(max_workers=args.workers) as pool:
        futures = {
            pool.submit(process_page, client, url, out_root,
                        page_map, downloader, args.save_md): url
            for url in targets
        }
        for i, future in enumerate(as_completed(futures), 1):
            url = futures[future]
            try:
                status, local, message = future.result()
                if status == "ok":
                    ok.append(local)
                else:
                    skipped.append((url, message))
            except Exception as exc:  # noqa: BLE001
                failed.append((url, str(exc)))
            if i % 50 == 0 or i == len(futures):
                LOG.info("进度 %d/%d（成功 %d，跳过 %d，失败 %d）",
                         i, len(futures), len(ok), len(skipped), len(failed))

    total = build_api_index(out_root)
    LOG.info("=" * 60)
    LOG.info("完成：成功 %d 页，跳过 %d 页，失败 %d 页，图片 %d 张（失败 %d）",
             len(ok), len(skipped), len(failed),
             len(downloader._done), len(downloader.failures))
    for url, reason in failed[:20]:
        LOG.warning("  失败: %s — %s", url, reason)
    for url, reason in skipped[:20]:
        LOG.info("  跳过: %s — %s", url, reason)
    LOG.info("API 目录已重建，共 %d 页: %s", total, (out_root / "api" / "index.html").resolve())
    return 0


if __name__ == "__main__":
    sys.exit(main())
