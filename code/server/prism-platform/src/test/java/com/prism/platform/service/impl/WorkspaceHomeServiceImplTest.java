package com.prism.platform.service.impl;

import com.prism.platform.mapper.WorkspaceMemberMapper;
import com.prism.platform.service.support.WorkspaceStatsContributor;
import com.prism.platform.vo.WorkspaceHomeStatsVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.ObjectProvider;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * 空间首页服务测试（memberCount 真实统计 / Contributor 聚合与降级 / 空数组打桩）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("空间首页服务")
class WorkspaceHomeServiceImplTest {

    private static final String CODE = "SMART-MFG";

    @Mock
    private WorkspaceMemberMapper workspaceMemberMapper;

    @Mock
    private ObjectProvider<WorkspaceStatsContributor> statsContributors;

    private WorkspaceHomeServiceImpl homeService;

    @BeforeEach
    void setUp() {
        homeService = new WorkspaceHomeServiceImpl(workspaceMemberMapper, statsContributors);
    }

    private void mockContributors(WorkspaceStatsContributor... contributors) {
        when(statsContributors.orderedStream()).thenReturn(Stream.of(contributors));
    }

    private WorkspaceStatsContributor contributor(String module, Map<String, Long> stats) {
        return new WorkspaceStatsContributor() {
            @Override
            public String module() {
                return module;
            }

            @Override
            public Map<String, Long> stats(String workspaceCode) {
                return stats;
            }
        };
    }

    @Test
    @DisplayName("stats：无 Contributor 时 memberCount 真实、其余指标为 0、环比为 null")
    void stats_noContributors_returnsRealMemberCountAndZeros() {
        when(workspaceMemberMapper.countEffectiveMembers(CODE, null, null)).thenReturn(42);
        mockContributors();

        WorkspaceHomeStatsVO vo = homeService.stats(CODE);

        assertThat(vo.getMemberCount()).isEqualTo(42);
        assertThat(vo.getOntologyAssetCount()).isZero();
        assertThat(vo.getDatasetCount()).isZero();
        assertThat(vo.getPipelineCount()).isZero();
        assertThat(vo.getApiCallToday()).isZero();
        assertThat(vo.getMemberCountTrend()).isNull();
        assertThat(vo.getOntologyAssetTrend()).isNull();
        assertThat(vo.getDatasetTrend()).isNull();
        assertThat(vo.getPipelineTrend()).isNull();
        assertThat(vo.getApiCallTrend()).isNull();
    }

    @Test
    @DisplayName("stats：多个 Contributor 的指标按键聚合")
    void stats_multipleContributors_mergesByKey() {
        when(workspaceMemberMapper.countEffectiveMembers(CODE, null, null)).thenReturn(7);
        mockContributors(
                contributor("ontology", Map.of("ontologyAssetCount", 86L)),
                contributor("data", Map.of("datasetCount", 12L, "pipelineCount", 3L)),
                contributor("application", Map.of("apiCallToday", 1024L)));

        WorkspaceHomeStatsVO vo = homeService.stats(CODE);

        assertThat(vo.getMemberCount()).isEqualTo(7);
        assertThat(vo.getOntologyAssetCount()).isEqualTo(86);
        assertThat(vo.getDatasetCount()).isEqualTo(12);
        assertThat(vo.getPipelineCount()).isEqualTo(3);
        assertThat(vo.getApiCallToday()).isEqualTo(1024L);
    }

    @Test
    @DisplayName("stats：Contributor 抛异常按 0 处理且不影响其他模块")
    void stats_contributorThrows_degradesToZero() {
        when(workspaceMemberMapper.countEffectiveMembers(CODE, null, null)).thenReturn(5);
        WorkspaceStatsContributor broken = new WorkspaceStatsContributor() {
            @Override
            public String module() {
                return "broken";
            }

            @Override
            public Map<String, Long> stats(String workspaceCode) {
                throw new IllegalStateException("boom");
            }
        };
        mockContributors(broken, contributor("data", Map.of("datasetCount", 9L)));

        WorkspaceHomeStatsVO vo = homeService.stats(CODE);

        assertThat(vo.getOntologyAssetCount()).isZero();
        assertThat(vo.getDatasetCount()).isEqualTo(9);
    }

    @Test
    @DisplayName("stats：Contributor 返回 null 时忽略")
    void stats_contributorReturnsNull_ignored() {
        when(workspaceMemberMapper.countEffectiveMembers(CODE, null, null)).thenReturn(0);
        mockContributors(contributor("ontology", null));

        WorkspaceHomeStatsVO vo = homeService.stats(CODE);

        assertThat(vo.getOntologyAssetCount()).isZero();
    }

    @Test
    @DisplayName("pipelines / activities：本期打桩返回空数组")
    void pipelinesAndActivities_returnEmpty() {
        assertThat(homeService.pipelines(CODE)).isEmpty();
        assertThat(homeService.activities(CODE)).isEmpty();
    }
}
