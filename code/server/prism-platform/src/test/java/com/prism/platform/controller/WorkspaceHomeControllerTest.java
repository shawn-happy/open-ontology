package com.prism.platform.controller;

import com.prism.common.exception.BusinessException;
import com.prism.common.exception.GlobalExceptionHandler;
import com.prism.platform.service.WorkspaceHomeService;
import com.prism.platform.vo.HomeActivityVO;
import com.prism.platform.vo.HomePipelineVO;
import com.prism.platform.vo.WorkspaceHomeStatsVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 空间首页控制器 MockMvc 薄壳测试（路由 / 路径变量绑定 / 统一响应结构）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("空间首页控制器")
class WorkspaceHomeControllerTest {

    private static final String CODE = "SMART-MFG";
    private static final String BASE = "/web/workspace/" + CODE + "/home";

    @Mock
    private WorkspaceHomeService workspaceHomeService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new WorkspaceHomeController(workspaceHomeService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /stats：以路径空间编码返回统计卡片")
    void stats_validWorkspaceCode_returnsStats() throws Exception {
        when(workspaceHomeService.stats(CODE)).thenReturn(WorkspaceHomeStatsVO.builder()
                .memberCount(10)
                .memberCountTrend(5.5)
                .ontologyAssetCount(0)
                .datasetCount(0)
                .pipelineCount(0)
                .apiCallToday(0L)
                .build());

        mockMvc.perform(get(BASE + "/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data.memberCount").value(10))
                .andExpect(jsonPath("$.data.memberCountTrend").value(5.5))
                .andExpect(jsonPath("$.data.apiCallToday").value(0));

        verify(workspaceHomeService).stats(CODE);
    }

    @Test
    @DisplayName("GET /stats：空间不存在 → 透出业务码 020103")
    void stats_workspaceNotFound_propagatesBusinessCode() throws Exception {
        when(workspaceHomeService.stats(CODE)).thenThrow(new BusinessException("020103", "空间不存在"));

        mockMvc.perform(get(BASE + "/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("020103"));
    }

    @Test
    @DisplayName("GET /pipelines：以路径空间编码返回 Pipeline 列表")
    void pipelines_validWorkspaceCode_returnsList() throws Exception {
        when(workspaceHomeService.pipelines(CODE)).thenReturn(List.of(HomePipelineVO.builder()
                .id(1L)
                .name("每日同步")
                .status("RUNNING")
                .progress(60)
                .build()));

        mockMvc.perform(get(BASE + "/pipelines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data[0].name").value("每日同步"))
                .andExpect(jsonPath("$.data[0].progress").value(60));

        verify(workspaceHomeService).pipelines(CODE);
    }

    @Test
    @DisplayName("GET /pipelines：本期无数据 → 返回空数组")
    void pipelines_noData_returnsEmptyArray() throws Exception {
        when(workspaceHomeService.pipelines(CODE)).thenReturn(List.of());

        mockMvc.perform(get(BASE + "/pipelines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());

        verify(workspaceHomeService).pipelines(CODE);
    }

    @Test
    @DisplayName("GET /activities：以路径空间编码返回近期动态列表")
    void activities_validWorkspaceCode_returnsList() throws Exception {
        when(workspaceHomeService.activities(CODE)).thenReturn(List.of(HomeActivityVO.builder()
                .id(1L)
                .type("ASSET")
                .content("创建对象类型")
                .operatorName("张三")
                .build()));

        mockMvc.perform(get(BASE + "/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data[0].type").value("ASSET"))
                .andExpect(jsonPath("$.data[0].operatorName").value("张三"));

        verify(workspaceHomeService).activities(CODE);
    }
}
