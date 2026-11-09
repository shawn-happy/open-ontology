package com.prism.platform.service.impl;

import com.prism.platform.mapper.WorkspaceMemberMapper;
import com.prism.platform.service.WorkspaceHomeService;
import com.prism.platform.service.support.WorkspaceStatsContributor;
import com.prism.platform.vo.HomeActivityVO;
import com.prism.platform.vo.HomePipelineVO;
import com.prism.platform.vo.WorkspaceHomeStatsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 空间首页服务实现
 * <p>
 * 本期口径：memberCount 真实统计；资产 / 数据集 / Pipeline / API 调用为跨模块扩展点，
 * 聚合已注册的 WorkspaceStatsContributor，未注册模块返回 0；pipelines / activities 返回空数组。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceHomeServiceImpl implements WorkspaceHomeService {

    private final WorkspaceMemberMapper workspaceMemberMapper;
    private final ObjectProvider<WorkspaceStatsContributor> statsContributors;

    @Override
    public WorkspaceHomeStatsVO stats(String workspaceCode) {
        int memberCount = workspaceMemberMapper.countEffectiveMembers(workspaceCode, null, null);

        Map<String, Long> merged = new HashMap<>();
        statsContributors.orderedStream().forEach(contributor -> {
            try {
                Map<String, Long> stats = contributor.stats(workspaceCode);
                if (stats != null) {
                    merged.putAll(stats);
                }
            }
            catch (Exception e) {
                log.warn("空间统计扩展点执行失败，按 0 处理：module={}, workspace={}",
                        contributor.module(), workspaceCode, e);
            }
        });

        return WorkspaceHomeStatsVO.builder()
                .memberCount(memberCount)
                .memberCountTrend(null)
                .ontologyAssetCount(value(merged, "ontologyAssetCount"))
                .ontologyAssetTrend(null)
                .datasetCount(value(merged, "datasetCount"))
                .datasetTrend(null)
                .pipelineCount(value(merged, "pipelineCount"))
                .pipelineTrend(null)
                .apiCallToday(merged.getOrDefault("apiCallToday", 0L))
                .apiCallTrend(null)
                .build();
    }

    @Override
    public List<HomePipelineVO> pipelines(String workspaceCode) {
        // 数据模块上线后接入，本期空数组（前端空态）
        return Collections.emptyList();
    }

    @Override
    public List<HomeActivityVO> activities(String workspaceCode) {
        // 审计模块接入后填充，本期空数组（前端空态）
        return Collections.emptyList();
    }

    private int value(Map<String, Long> merged, String key) {
        return merged.getOrDefault(key, 0L).intValue();
    }
}
