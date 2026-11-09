package com.prism.platform.service.support;

import java.util.Map;

/**
 * 空间首页统计扩展点 SPI
 * <p>
 * 本体 / 数据 / 应用模块上线后各自注册实现（Spring Bean 即注册），
 * HomeService 聚合各模块统计；未注册模块返回 0。
 */
public interface WorkspaceStatsContributor {

    /**
     * 模块标识（如 ontology / data / application）
     */
    String module();

    /**
     * 空间维度统计
     *
     * @param workspaceCode 空间编码
     * @return 指标名 → 数值（如 ontologyAssetCount → 86）
     */
    Map<String, Long> stats(String workspaceCode);
}
