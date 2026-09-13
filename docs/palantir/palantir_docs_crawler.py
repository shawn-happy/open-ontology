#!/usr/bin/env python3
"""Palantir Foundry 文档爬虫。

爬取指定能力板块（Capabilities）的文档页面以及 Foundry API v2 全部页面：
  - 通过各板块落地页的侧边栏导航树发现所有页面 URL
  - 每个页面从 __NEXT_DATA__ 中提取原始 Markdown
  - 下载 Markdown 中引用的图片到本地 `_resources/`
  - 将 Markdown 渲染为 HTML，并重写站内链接与图片路径为本地相对路径
  - 生成 index.html 作为目录索引

用法示例:
    uv run palantir_docs_crawler.py                          # 爬取全部目标板块 + API v2
    uv run palantir_docs_crawler.py --sections ontology      # 只爬某个板块
    uv run palantir_docs_crawler.py --no-api                 # 不爬 API v2
    uv run palantir_docs_crawler.py --max-pages 20           # 限制页面数（调试用）
"""

from __future__ import annotations

import argparse
import hashlib
import json
import logging
import os
import re
import sys
import threading
import time
from concurrent.futures import ThreadPoolExecutor, as_completed
from dataclasses import dataclass, field
from pathlib import Path
from typing import Any
from urllib.parse import urljoin, urlparse

import markdown as md_lib
import requests

LOG = logging.getLogger("palantir-crawler")

BASE_URL = "https://www.palantir.com"
DOCS_BASE = f"{BASE_URL}/docs/foundry"
NEXT_DATA_RE = re.compile(r'<script id="__NEXT_DATA__"[^>]*>(.*?)</script>', re.S)

# 目标板块：名称 -> 能力落地页（其侧边栏包含该板块全部页面）
INCLUDED_SECTIONS: dict[str, str] = {
    "aip": "/docs/foundry/aip/overview/",
    "data-integration": "/docs/foundry/data-integration/overview/",
    "ontology": "/docs/foundry/ontology/overview/",
    "dev-toolchain": "/docs/foundry/dev-toolchain/overview/",
    "security": "/docs/foundry/security/overview/",
}

# 其余板块落地页：用于把交叉链接页面从目标集合中剔除
EXCLUDED_SECTIONS: dict[str, str] = {
    "model-integration": "/docs/foundry/model-integration/overview/",
    "app-building": "/docs/foundry/app-building/overview/",
    "observability": "/docs/foundry/observability/overview/",
    "analytics": "/docs/foundry/analytics/overview/",
    "devops": "/docs/foundry/devops/overview/",
    "administration": "/docs/foundry/administration/overview/",
    "getting-started": "/docs/foundry/getting-started/overview/",
    "architecture-center": "/docs/foundry/architecture-center/overview/",
}

API_SEED_URL = "/docs/foundry/api/v2"
API_URL_PREFIX = "/foundry/api/v2/"

SECTION_LABELS = {
    "aip": "AI Platform (AIP)",
    "data-integration": "Data connectivity & integration",
    "ontology": "Ontology building",
    "dev-toolchain": "Developer toolchain",
    "security": "Security & governance",
    "data-connection": "Data connection",
    "connectors": "Connectors",
    "hyperauto": "HyperAuto",
    "api": "Foundry API v2",
}

HTML_TEMPLATE = """<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>{title}</title>
<style>
  body {{ font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
         max-width: 900px; margin: 0 auto; padding: 24px; line-height: 1.65; color: #1c2b33; }}
  img {{ max-width: 100%; height: auto; }}
  pre {{ background: #f4f6f8; padding: 12px; border-radius: 6px; overflow-x: auto; }}
  code {{ background: #f4f6f8; padding: 1px 4px; border-radius: 3px; font-size: 0.92em; }}
  pre code {{ background: none; padding: 0; }}
  table {{ border-collapse: collapse; width: 100%; margin: 16px 0; }}
  th, td {{ border: 1px solid #d5dce2; padding: 6px 10px; text-align: left; }}
  th {{ background: #f0f3f6; }}
  h1, h2, h3 {{ color: #0e3a2f; }}
  a {{ color: #0e6f5c; }}
  .site-header {{ border-bottom: 1px solid #e2e7eb; padding-bottom: 12px; margin-bottom: 24px;
                  font-size: 0.9em; color: #5b6b74; }}
</style>
</head>
<body>
<div class="site-header">Palantir Foundry 文档镜像 · <a href="{index_link}">返回目录</a></div>
{content}
</body>
</html>
"""


# --------------------------------------------------------------------------- #
# HTTP 客户端
# --------------------------------------------------------------------------- #
class DocClient:
    """带重试与限速的 HTTP 客户端（线程安全：每个线程使用独立 Session）。"""

    def __init__(self, workers: int = 6, timeout: int = 30, max_retries: int = 3):
        self.timeout = timeout
        self.max_retries = max_retries
        self.delay = max(0.1, 0.5 / workers)
        self._local = threading.local()

    def _session(self) -> requests.Session:
        session = getattr(self._local, "session", None)
        if session is None:
            session = requests.Session()
            session.headers.update(
                {
                    "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) "
                                  "docs-mirror-crawler/1.0",
                    "Accept-Language": "en-US,en;q=0.9",
                }
            )
            self._local.session = session
        return session

    def get(self, url: str, stream: bool = False) -> requests.Response:
        last_exc: Exception | None = None
        for attempt in range(1, self.max_retries + 1):
            try:
                time.sleep(self.delay)
                resp = self._session().get(url, timeout=self.timeout, stream=stream)
                if resp.status_code == 200:
                    return resp
                last_exc = RuntimeError(f"HTTP {resp.status_code}")
                LOG.debug("HTTP %s on %s (attempt %d)", resp.status_code, url, attempt)
            except requests.RequestException as exc:
                last_exc = exc
                LOG.debug("Request error on %s (attempt %d): %s", url, attempt, exc)
            time.sleep(2**attempt)
        raise RuntimeError(f"failed after {self.max_retries} attempts: {url} ({last_exc})")

    def get_html(self, url: str) -> str:
        return self.get(url).text

    def get_json(self, url: str) -> Any:
        return json.loads(self.get(url).content)


# --------------------------------------------------------------------------- #
# 页面发现
# --------------------------------------------------------------------------- #
def extract_next_data(html: str) -> dict[str, Any]:
    match = NEXT_DATA_RE.search(html)
    if not match:
        raise ValueError("__NEXT_DATA__ not found")
    return json.loads(match.group(1))


def collect_sidebar_urls(items: list[dict[str, Any]] | None) -> set[str]:
    """递归收集侧边栏树中所有本站页面链接（过滤外链与带查询参数的链接）。"""
    urls: set[str] = set()
    for item in items or []:
        item_type = item.get("type")
        if item_type == "pageLink":
            link = item.get("link") or {}
            url = link.get("url")
            if (
                url
                and not link.get("openInNewTab")
                and url.startswith("/")
                and "?" not in url
                and "#" not in url
            ):
                urls.add(url.rstrip("/") + "/")
        elif item_type == "section":
            urls |= collect_sidebar_urls(item.get("items"))
    return urls


def get_page_props(client: DocClient, docs_path: str) -> dict[str, Any]:
    """docs_path 为 /docs/foundry/... 形式的路径。"""
    html = client.get_html(BASE_URL + docs_path)
    data = extract_next_data(html)
    return data["props"]["pageProps"]


def discover_pages(client: DocClient, sections: list[str], include_api: bool) -> dict[str, str]:
    """发现所有待爬取页面，返回 {页面 URL: 所属板块目录名}。"""
    section_of: dict[str, str] = {}
    for name in sections:
        seed = INCLUDED_SECTIONS[name]
        LOG.info("发现板块 %-18s %s", name, seed)
        props = get_page_props(client, seed)
        nav = props.get("sidebarNavProps") or {}
        urls = collect_sidebar_urls(nav.get("items"))
        LOG.info("  侧边栏页面数: %d", len(urls))
        for url in urls:
            # 同一页面出现在多个板块时，保留首个归属
            section_of.setdefault(url, name)

    excluded: set[str] = set()
    for name, seed in EXCLUDED_SECTIONS.items():
        try:
            props = get_page_props(client, seed)
            nav = props.get("sidebarNavProps") or {}
            excluded |= collect_sidebar_urls(nav.get("items"))
        except Exception as exc:  # noqa: BLE001 - 排除集失败不影响主流程
            LOG.warning("获取排除板块 %s 侧边栏失败: %s", name, exc)
    for url in excluded:
        section_of.pop(url, None)

    if include_api:
        props = get_page_props(client, API_SEED_URL)
        api_urls = collect_sidebar_urls(props.get("sidebarNavItems"))
        api_urls = {u for u in api_urls if u.startswith(API_URL_PREFIX)}
        LOG.info("发现 API v2 页面数: %d", len(api_urls))
        for url in api_urls:
            section_of.setdefault(url, "api")

    return section_of


# --------------------------------------------------------------------------- #
# 内容提取与路径映射
# --------------------------------------------------------------------------- #
def extract_markdown(props: dict[str, Any]) -> tuple[str | None, str]:
    """返回 (markdown, 标题)。文档页与 API 页的数据结构不同。"""
    markdown_text = props.get("markdown")
    title = (props.get("metadata") or {}).get("title") or ""
    if not markdown_text:
        page = props.get("page")
        if isinstance(page, dict):
            content = page.get("content") or {}
            if content.get("type") == "markdown":
                markdown_text = content.get("markdown")
                title = title or page.get("title") or ""
    if markdown_text and not title:
        match = re.search(r"^#\s+(.+)$", markdown_text, re.M)
        title = match.group(1).strip() if match else ""
    return markdown_text, title


def url_to_relpath(url: str, section: str) -> str:
    """映射为 <板块目录>/xxx.html 的相对路径。"""
    path = url.split("?")[0].split("#")[0].rstrip("/")
    if section == "api":
        return "api/" + path.removeprefix("/foundry/api/") + ".html"
    rest = path.removeprefix("/foundry/")
    if rest.startswith(section + "/"):
        rest = rest[len(section) + 1:]
    return f"{section}/{rest}.html"


def resource_relpath(src: str) -> str:
    """/docs/resources/x.png -> _resources/x.png；外部图片按内容哈希命名。"""
    if src.startswith("/docs/resources/"):
        return "_resources/" + src.removeprefix("/docs/resources/").lstrip("/")
    parsed = urlparse(src)
    ext = Path(parsed.path).suffix or ".bin"
    digest = hashlib.md5(src.encode()).hexdigest()[:12]
    return f"_resources/external-{digest}{ext}"


# --------------------------------------------------------------------------- #
# 图片下载与链接重写
# --------------------------------------------------------------------------- #
MD_IMG_RE = re.compile(r"(!\[[^\]]*\]\(\s*)([^)\s]+)([^)]*\))")
HTML_IMG_SRC_RE = re.compile(r'(<img\s+[^>]*?src=")([^"]+)(")', re.I)
MD_LINK_RE = re.compile(r"(\]\()(/docs/foundry/[^)\s]+)([^)]*\))")
HTML_LINK_RE = re.compile(r'(href=")(/docs/foundry/[^"]+)(")')


class AssetDownloader:
    """线程安全的图片下载器：去重判断与记录均受锁保护，文件原子写入。"""

    def __init__(self, client: DocClient, out_root: Path):
        self.client = client
        self.out_root = out_root
        self._done: set[str] = set()
        self.failures: dict[str, str] = {}
        self._lock = threading.Lock()

    def download(self, src: str, page_url: str) -> str | None:
        """下载图片，返回本地相对 `_resources/...` 路径；失败返回 None。

        并发正确性：已下载判定在锁内完成；写入先用临时文件再
        os.replace 原子替换，多线程同时写同一目标也不会产生损坏文件。
        """
        target = resource_relpath(src)
        with self._lock:
            if target in self._done:
                return target
        abs_url = urljoin(page_url, src)
        try:
            resp = self.client.get(abs_url)
            dest = self.out_root / target
            dest.parent.mkdir(parents=True, exist_ok=True)
            tmp = dest.with_name(dest.name + f".tmp-{threading.get_ident()}")
            tmp.write_bytes(resp.content)
            os.replace(tmp, dest)  # 原子替换，避免并发写损坏
            with self._lock:
                self._done.add(target)
            return target
        except Exception as exc:  # noqa: BLE001
            with self._lock:
                self.failures[src] = str(exc)
            return None


def rel_link(from_html: str, to_path: str) -> str:
    return os.path.relpath(to_path, start=os.path.dirname(from_html))


def rewrite_markdown(
    markdown_text: str,
    page_html_path: str,
    page_url: str,
    downloader: AssetDownloader,
    rel_pages: dict[str, str],
) -> str:
    """重写图片与站内链接为本地相对路径。"""

    def image_sub(match: re.Match) -> str:
        src = match.group(2)
        local = downloader.download(src, page_url)
        if not local:
            return match.group(0)
        return match.group(1) + rel_link(page_html_path, local) + match.group(3)

    def doc_link_sub(match: re.Match) -> str:
        url = match.group(2).rstrip("/") + "/"
        target = rel_pages.get(url)
        if target:
            return match.group(1) + rel_link(page_html_path, target) + match.group(3)
        return match.group(1) + BASE_URL + match.group(2) + match.group(3)

    text = MD_IMG_RE.sub(image_sub, markdown_text)
    text = HTML_IMG_SRC_RE.sub(lambda m: m.group(1) + (rel_link(
        page_html_path, downloader.download(m.group(2), page_url) or m.group(2)
    )) + m.group(3), text)
    text = MD_LINK_RE.sub(doc_link_sub, text)
    text = HTML_LINK_RE.sub(doc_link_sub, text)
    return text


# --------------------------------------------------------------------------- #
# 页面抓取
# --------------------------------------------------------------------------- #
@dataclass
class PageResult:
    url: str
    html_path: str
    title: str
    section: str


@dataclass
class CrawlStats:
    ok: list[PageResult] = field(default_factory=list)
    skipped: list[tuple[str, str]] = field(default_factory=list)
    failed: list[tuple[str, str]] = field(default_factory=list)


def crawl_page(
    client: DocClient,
    url: str,
    section: str,
    out_root: Path,
    rel_pages: dict[str, str],
    downloader: AssetDownloader,
    save_md: bool,
) -> PageResult:
    page_url = BASE_URL + "/docs" + url
    props = get_page_props(client, "/docs" + url)
    markdown_text, title = extract_markdown(props)
    if not markdown_text:
        raise ValueError("no markdown content (页面可能为跳转页或空页)")

    html_path = url_to_relpath(url, section)
    text = rewrite_markdown(markdown_text, html_path, page_url, downloader, rel_pages)

    html_body = md_lib.markdown(text, extensions=["tables", "fenced_code", "sane_lists"])
    html_doc = HTML_TEMPLATE.format(
        title=title or url,
        index_link=rel_link(html_path, "index.html"),
        content=html_body,
    )

    dest_html = out_root / html_path
    dest_html.parent.mkdir(parents=True, exist_ok=True)
    dest_html.write_text(html_doc, encoding="utf-8")
    if save_md:
        dest_md = dest_html.with_suffix(".md")
        dest_md.write_text(markdown_text, encoding="utf-8")
    return PageResult(url=url, html_path=html_path, title=title, section=section)


INDEX_STYLE = ("<style>body{font-family:-apple-system,sans-serif;max-width:900px;margin:0 auto;"
               "padding:24px;line-height:1.6}h2{color:#0e3a2f;border-bottom:1px solid #e2e7eb;"
               "padding-bottom:4px}li{margin:2px 0}a{color:#0e6f5c;text-decoration:none}"
               "a:hover{text-decoration:underline}</style>")


def build_index(out_root: Path, results: list[PageResult]) -> None:
    """生成根目录索引 + 每个板块目录各自的 index.html。"""
    groups: dict[str, list[PageResult]] = {}
    for result in results:
        groups.setdefault(result.section, []).append(result)

    root_parts = ["<!DOCTYPE html><html lang='zh'><head><meta charset='utf-8'>",
                  "<title>Palantir Foundry 文档镜像</title>", INDEX_STYLE, "</head><body>",
                  "<h1>Palantir Foundry 文档镜像</h1>",
                  f"<p>共 {len(results)} 个页面，点击板块进入对应目录。</p>"]
    for section in sorted(groups, key=lambda s: SECTION_LABELS.get(s, s).lower()):
        label = SECTION_LABELS.get(section, section)
        pages = sorted(groups[section], key=lambda r: r.html_path)

        section_parts = ["<!DOCTYPE html><html lang='zh'><head><meta charset='utf-8'>",
                         f"<title>{label} · 文档目录</title>", INDEX_STYLE, "</head><body>",
                         f"<h1>{label}</h1>",
                         f"<p><a href='../index.html'>← 返回总目录</a> · 共 {len(pages)} 个页面</p>",
                         "<ul>"]
        for result in pages:
            rel = result.html_path.removeprefix(section + "/")
            section_parts.append(
                f"<li><a href='{rel}'>{result.title or result.url}</a></li>")
        section_parts.append("</ul></body></html>")
        section_dir = out_root / section
        section_dir.mkdir(parents=True, exist_ok=True)
        (section_dir / "index.html").write_text("\n".join(section_parts), encoding="utf-8")

        root_parts.append(
            f"<h2>{label}</h2>"
            f"<p><a href='{section}/index.html'>进入 {label} 目录（{len(pages)} 页）</a></p>")
    root_parts.append("</body></html>")
    (out_root / "index.html").write_text("\n".join(root_parts), encoding="utf-8")


# --------------------------------------------------------------------------- #
# 主流程
# --------------------------------------------------------------------------- #
def parse_args(argv: list[str] | None = None) -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Palantir Foundry 文档爬虫")
    parser.add_argument(
        "--sections",
        default=",".join(INCLUDED_SECTIONS),
        help="逗号分隔的板块名，可选: " + ",".join(INCLUDED_SECTIONS),
    )
    parser.add_argument("--no-api", action="store_true", help="不爬取 API v2 文档")
    parser.add_argument("--output", default="palantir_docs", help="输出目录")
    parser.add_argument("--workers", type=int, default=6, help="并发线程数")
    parser.add_argument("--max-pages", type=int, default=0, help="限制页面数（0 = 不限制）")
    parser.add_argument("--save-md", action="store_true", help="同时保存原始 Markdown 文件")
    parser.add_argument("-v", "--verbose", action="store_true")
    return parser.parse_args(argv)


def main(argv: list[str] | None = None) -> int:
    args = parse_args(argv)
    logging.basicConfig(
        level=logging.DEBUG if args.verbose else logging.INFO,
        format="%(asctime)s %(levelname)s %(message)s",
        datefmt="%H:%M:%S",
    )

    sections = [s.strip() for s in args.sections.split(",") if s.strip()]
    unknown = [s for s in sections if s not in INCLUDED_SECTIONS]
    if unknown:
        LOG.error("未知板块: %s（可选: %s）", ", ".join(unknown), ", ".join(INCLUDED_SECTIONS))
        return 2
    include_api = not args.no_api
    out_root = Path(args.output)
    out_root.mkdir(parents=True, exist_ok=True)

    client = DocClient(workers=args.workers)
    pages_map = discover_pages(client, sections, include_api)
    if args.max_pages > 0:
        pages_map = dict(sorted(pages_map.items())[: args.max_pages])
    LOG.info("待爬取页面总数: %d", len(pages_map))

    downloader = AssetDownloader(client, out_root)
    stats = CrawlStats()

    with ThreadPoolExecutor(max_workers=args.workers) as pool:
        futures = {
            pool.submit(
                crawl_page, client, url, section, out_root,
                pages_map, downloader, args.save_md,
            ): url
            for url, section in pages_map.items()
        }
        for i, future in enumerate(as_completed(futures), 1):
            url = futures[future]
            try:
                stats.ok.append(future.result())
            except Exception as exc:  # noqa: BLE001
                message = str(exc)
                if "no markdown" in message:
                    stats.skipped.append((url, message))
                else:
                    stats.failed.append((url, message))
            if i % 25 == 0 or i == len(futures):
                LOG.info("进度 %d/%d（成功 %d，跳过 %d，失败 %d）",
                         i, len(futures), len(stats.ok), len(stats.skipped), len(stats.failed))

    build_index(out_root, stats.ok)

    LOG.info("=" * 60)
    LOG.info("完成：成功 %d 页，跳过 %d 页，失败 %d 页，图片 %d 张（失败 %d）",
             len(stats.ok), len(stats.skipped), len(stats.failed),
             len(downloader._done), len(downloader.failures))
    for url, reason in stats.failed[:20]:
        LOG.warning("  失败: %s — %s", url, reason)
    for url, reason in stats.skipped[:20]:
        LOG.info("  跳过: %s — %s", url, reason)
    for src, reason in list(downloader.failures.items())[:20]:
        LOG.warning("  图片失败: %s — %s", src, reason)
    LOG.info("输出目录: %s", out_root.resolve())
    return 0


if __name__ == "__main__":
    sys.exit(main())
