package com.prism.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prism.common.exception.BusinessException;
import com.prism.common.exception.GlobalExceptionHandler;
import com.prism.common.result.PageResult;
import com.prism.platform.dto.MemberPageQuery;
import com.prism.platform.dto.MemberRemoveDTO;
import com.prism.platform.dto.MemberRoleDTO;
import com.prism.platform.service.WorkspaceMemberService;
import com.prism.platform.vo.MemberListVO;
import com.prism.platform.vo.MemberStatsVO;
import com.prism.platform.vo.WorkspaceRoleVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 空间成员控制器 MockMvc 薄壳测试（路由 / 参数绑定 / 统一响应结构）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("空间成员控制器")
class WorkspaceMemberControllerTest {

    private static final String CODE = "SMART-MFG";
    private static final String BASE = "/web/workspace/" + CODE;

    @Mock
    private WorkspaceMemberService workspaceMemberService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new WorkspaceMemberController(workspaceMemberService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /members：分页参数与过滤条件正确绑定，返回 PageResult 结构")
    void pageMembers_bindsQueryAndReturnsPageResult() throws Exception {
        MemberListVO vo = MemberListVO.builder()
                .userId(100L)
                .username("zhangsan")
                .name("张三")
                .workspaceRole("OWNER")
                .roleLevel(5)
                .source("DIRECT")
                .build();
        PageResult<MemberListVO> page = PageResult.<MemberListVO>builder()
                .data(List.of(vo))
                .pageInfo(PageResult.PageInfo.builder()
                        .currentPage(1)
                        .pageSize(20)
                        .total(1L)
                        .totalPages(1)
                        .build())
                .build();
        when(workspaceMemberService.pageMembers(eq(CODE), any(MemberPageQuery.class)))
                .thenReturn(page);

        mockMvc.perform(get(BASE + "/members")
                .param("currentPage", "1")
                .param("pageSize", "20")
                .param("keyword", "zhang")
                .param("workspaceRole", "OWNER")
                .param("source", "DIRECT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data.data[0].userId").value(100))
                .andExpect(jsonPath("$.data.data[0].workspaceRole").value("OWNER"))
                .andExpect(jsonPath("$.data.pageInfo.total").value(1));

        ArgumentCaptor<MemberPageQuery> captor = ArgumentCaptor.forClass(MemberPageQuery.class);
        verify(workspaceMemberService).pageMembers(eq(CODE), captor.capture());
        MemberPageQuery query = captor.getValue();
        assertThat(query.getCurrentPage()).isEqualTo(1);
        assertThat(query.getPageSize()).isEqualTo(20);
        assertThat(query.getKeyword()).isEqualTo("zhang");
        assertThat(query.getWorkspaceRole()).isEqualTo("OWNER");
        assertThat(query.getSource()).isEqualTo("DIRECT");
    }

    @Test
    @DisplayName("GET /members/stats：返回四项统计")
    void stats_returnsFourCounts() throws Exception {
        MemberStatsVO stats = MemberStatsVO.builder()
                .totalMembers(10)
                .directCount(4)
                .deptCount(6)
                .ownerCount(1)
                .build();
        when(workspaceMemberService.getMemberStats(CODE)).thenReturn(stats);

        mockMvc.perform(get(BASE + "/members/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data.totalMembers").value(10))
                .andExpect(jsonPath("$.data.directCount").value(4))
                .andExpect(jsonPath("$.data.deptCount").value(6))
                .andExpect(jsonPath("$.data.ownerCount").value(1));
    }

    @Test
    @DisplayName("GET /roles：返回角色列表")
    void roles_returnsList() throws Exception {
        WorkspaceRoleVO role = WorkspaceRoleVO.builder()
                .code("OWNER")
                .displayName("拥有者")
                .level(5)
                .builtin(true)
                .build();
        when(workspaceMemberService.listRoles(CODE)).thenReturn(List.of(role));

        mockMvc.perform(get(BASE + "/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data[0].code").value("OWNER"))
                .andExpect(jsonPath("$.data[0].builtin").value(true));
    }

    @Test
    @DisplayName("POST /members/role：正常请求转发服务并返回成功")
    void updateRole_validRequest_delegatesToService() throws Exception {
        mockMvc.perform(post(BASE + "/members/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("userId", 100, "workspaceRole", "DEVELOPER"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        ArgumentCaptor<MemberRoleDTO> captor = ArgumentCaptor.forClass(MemberRoleDTO.class);
        verify(workspaceMemberService).updateMemberRole(eq(CODE), captor.capture());
        assertThat(captor.getValue().getUserId()).isEqualTo(100L);
        assertThat(captor.getValue().getWorkspaceRole()).isEqualTo("DEVELOPER");
    }

    @Test
    @DisplayName("POST /members/role：userId 缺失 → 999901 参数校验失败")
    void updateRole_missingUserId_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/members/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("workspaceRole", "DEVELOPER"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }

    @Test
    @DisplayName("POST /members/remove：服务抛业务异常 → 透出业务码 020209")
    void removeMember_serviceThrows_propagatesBusinessCode() throws Exception {
        doThrow(new BusinessException("020209", "该成员由部门分配覆盖，无法直接移除"))
                .when(workspaceMemberService)
                .removeMember(eq(CODE), any(MemberRemoveDTO.class));

        mockMvc.perform(post(BASE + "/members/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("userId", 100))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("020209"));
    }
}
