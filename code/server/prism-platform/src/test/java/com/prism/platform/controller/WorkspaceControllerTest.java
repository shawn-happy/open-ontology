package com.prism.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prism.common.exception.BusinessException;
import com.prism.common.exception.GlobalExceptionHandler;
import com.prism.common.result.PageResult;
import com.prism.common.security.SecurityUtils;
import com.prism.platform.dto.WorkspaceSaveDTO;
import com.prism.platform.dto.WorkspacePageQuery;
import com.prism.platform.service.WorkspaceMemberService;
import com.prism.platform.service.WorkspaceService;
import com.prism.platform.vo.WorkspaceListVO;
import com.prism.platform.vo.WorkspaceOverviewVO;
import com.prism.platform.vo.WorkspaceSimpleVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 工作空间管理控制器 MockMvc 薄壳测试（路由 / 参数绑定与校验 / 统一响应结构）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("工作空间管理控制器")
class WorkspaceControllerTest {

    private static final String BASE = "/web/platform/workspaces";
    private static final String CODE = "SMART-MFG";
    private static final Long USER_ID = 20L;

    @Mock
    private WorkspaceService workspaceService;

    @Mock
    private WorkspaceMemberService workspaceMemberService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockedStatic<SecurityUtils> securityUtils;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new WorkspaceController(workspaceService, workspaceMemberService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        securityUtils = Mockito.mockStatic(SecurityUtils.class);
        securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(USER_ID);
    }

    @AfterEach
    void tearDown() {
        securityUtils.close();
    }

    @Test
    @DisplayName("GET /web/platform/workspaces：分页查询绑定并返回 PageResult 结构")
    void page_bindsQueryAndReturnsPageResult() throws Exception {
        WorkspaceListVO vo = WorkspaceListVO.builder()
                .id(1L)
                .code(CODE)
                .name("智能制造空间")
                .status("ENABLED")
                .memberCount(10)
                .build();
        PageResult<WorkspaceListVO> page = PageResult.<WorkspaceListVO>builder()
                .data(List.of(vo))
                .pageInfo(PageResult.PageInfo.builder()
                        .currentPage(1)
                        .pageSize(20)
                        .total(1L)
                        .totalPages(1)
                        .build())
                .build();
        when(workspaceService.page(any(WorkspacePageQuery.class))).thenReturn(page);

        mockMvc.perform(get(BASE).param("currentPage", "1").param("keyword", "SMART"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data.data[0].code").value(CODE))
                .andExpect(jsonPath("$.data.pageInfo.total").value(1));

        ArgumentCaptor<WorkspacePageQuery> captor = ArgumentCaptor.forClass(WorkspacePageQuery.class);
        verify(workspaceService).page(captor.capture());
        assertThat(captor.getValue().getKeyword()).isEqualTo("SMART");
    }

    @Test
    @DisplayName("GET /overview：返回平台概览统计")
    void overview_returnsStats() throws Exception {
        when(workspaceService.overview()).thenReturn(WorkspaceOverviewVO.builder()
                .totalSpaces(3)
                .totalMembers(25)
                .totalAssets(0)
                .activeSpaces30d(2)
                .build());

        mockMvc.perform(get(BASE + "/overview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data.totalSpaces").value(3))
                .andExpect(jsonPath("$.data.activeSpaces30d").value(2));
    }

    @Test
    @DisplayName("GET /accessible：以当前登录用户 ID 查询可访问空间")
    void accessible_usesCurrentUserId() throws Exception {
        when(workspaceService.accessible(USER_ID))
                .thenReturn(List.of(WorkspaceSimpleVO.builder()
                        .code(CODE)
                        .name("智能制造空间")
                        .workspaceRole("OWNER")
                        .build()));

        mockMvc.perform(get(BASE + "/accessible"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data[0].code").value(CODE))
                .andExpect(jsonPath("$.data[0].workspaceRole").value("OWNER"));

        verify(workspaceService).accessible(USER_ID);
    }

    @Test
    @DisplayName("GET /code/check：返回编码可用性布尔值")
    void checkCode_returnsBoolean() throws Exception {
        when(workspaceService.isCodeAvailable("NEW-CODE")).thenReturn(true);

        mockMvc.perform(get(BASE + "/code/check").param("code", "NEW-CODE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    @DisplayName("POST /create：编码格式非法 → 999901 参数校验失败")
    void create_invalidCode_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        Map.of("code", "bad_code", "name", "空间", "ownerUserId", 10))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }

    @Test
    @DisplayName("POST /create：合法请求返回新空间 ID")
    void create_validRequest_returnsId() throws Exception {
        when(workspaceService.create(any(WorkspaceSaveDTO.class))).thenReturn(9L);

        mockMvc.perform(post(BASE + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        Map.of("code", CODE, "name", "智能制造空间", "ownerUserId", 10))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data").value(9));

        ArgumentCaptor<WorkspaceSaveDTO> captor = ArgumentCaptor.forClass(WorkspaceSaveDTO.class);
        verify(workspaceService).create(captor.capture());
        assertThat(captor.getValue().getCode()).isEqualTo(CODE);
        assertThat(captor.getValue().getOwnerUserId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("POST /create：编码重复 → 透出业务码 020101")
    void create_codeExists_propagatesBusinessCode() throws Exception {
        when(workspaceService.create(any(WorkspaceSaveDTO.class)))
                .thenThrow(new BusinessException("020101", "空间编码已存在"));

        mockMvc.perform(post(BASE + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        Map.of("code", CODE, "name", "空间", "ownerUserId", 10))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("020101"));
    }

    @Test
    @DisplayName("POST /delete：空间不存在 → 透出业务码 020103")
    void delete_notFound_propagatesBusinessCode() throws Exception {
        doThrow(new BusinessException("020103", "空间不存在")).when(workspaceService).delete(99L);

        mockMvc.perform(post(BASE + "/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("id", 99))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("020103"));
    }
}
