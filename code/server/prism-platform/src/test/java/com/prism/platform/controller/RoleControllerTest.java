package com.prism.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prism.common.exception.BusinessException;
import com.prism.common.exception.GlobalExceptionHandler;
import com.prism.common.result.PageResult;
import com.prism.platform.dto.CreateRoleDTO;
import com.prism.platform.dto.RolePageQuery;
import com.prism.platform.dto.RolePermissionDTO;
import com.prism.platform.dto.UpdateRoleDTO;
import com.prism.platform.service.RoleService;
import com.prism.platform.vo.RoleDetailVO;
import com.prism.platform.vo.RoleListVO;
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
 * 角色管理控制器 MockMvc 薄壳测试（路由 / 参数绑定与校验 / 统一响应结构）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("角色管理控制器")
class RoleControllerTest {

    private static final String BASE = "/web/system/roles";

    @Mock
    private RoleService roleService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new RoleController(roleService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /system/roles：分页参数正确绑定，返回 PageResult 结构")
    void page_bindsQueryAndReturnsPageResult() throws Exception {
        RoleListVO vo = RoleListVO.builder()
                .id(2L)
                .name("数据开发者")
                .code("DATA_DEVELOPER")
                .type("GLOBAL")
                .builtin(false)
                .status("ENABLED")
                .memberCount(5)
                .build();
        PageResult<RoleListVO> page = PageResult.<RoleListVO>builder()
                .data(List.of(vo))
                .pageInfo(PageResult.PageInfo.builder()
                        .currentPage(1)
                        .pageSize(20)
                        .total(1L)
                        .totalPages(1)
                        .build())
                .build();
        when(roleService.page(any(RolePageQuery.class))).thenReturn(page);

        mockMvc.perform(get(BASE).param("currentPage", "1").param("keyword", "DATA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data.data[0].code").value("DATA_DEVELOPER"))
                .andExpect(jsonPath("$.data.pageInfo.total").value(1));

        ArgumentCaptor<RolePageQuery> captor = ArgumentCaptor.forClass(RolePageQuery.class);
        verify(roleService).page(captor.capture());
        assertThat(captor.getValue().getKeyword()).isEqualTo("DATA");
    }

    @Test
    @DisplayName("GET /system/roles/all：按关键词返回全部角色下拉列表")
    void listAll_withKeyword_returnsList() throws Exception {
        when(roleService.listAll("dev")).thenReturn(List.of(RoleListVO.builder()
                .id(2L)
                .name("数据开发者")
                .code("DATA_DEVELOPER")
                .build()));

        mockMvc.perform(get(BASE + "/all").param("keyword", "dev"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data[0].code").value("DATA_DEVELOPER"));

        verify(roleService).listAll("dev");
    }

    @Test
    @DisplayName("GET /system/roles/all：关键词缺省时以 null 调用服务")
    void listAll_withoutKeyword_passesNull() throws Exception {
        when(roleService.listAll(null)).thenReturn(List.of());

        mockMvc.perform(get(BASE + "/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data").isArray());

        verify(roleService).listAll(null);
    }

    @Test
    @DisplayName("GET /system/roles/{id}：返回角色详情")
    void getById_whenExists_returnsDetail() throws Exception {
        when(roleService.getById(2L)).thenReturn(RoleDetailVO.builder()
                .id(2L)
                .name("数据开发者")
                .code("DATA_DEVELOPER")
                .type("GLOBAL")
                .dataScope("DEPT")
                .menuIds(List.of(10L, 11L))
                .build());

        mockMvc.perform(get(BASE + "/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.dataScope").value("DEPT"))
                .andExpect(jsonPath("$.data.menuIds[1]").value(11));

        verify(roleService).getById(2L);
    }

    @Test
    @DisplayName("POST /system/roles/create：合法请求转发服务并返回成功")
    void create_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "name", "数据开发者",
                        "code", "DATA_DEVELOPER",
                        "type", "GLOBAL",
                        "remark", "备注"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        ArgumentCaptor<CreateRoleDTO> captor = ArgumentCaptor.forClass(CreateRoleDTO.class);
        verify(roleService).create(captor.capture());
        assertThat(captor.getValue().getCode()).isEqualTo("DATA_DEVELOPER");
        assertThat(captor.getValue().getType()).isEqualTo("GLOBAL");
    }

    @Test
    @DisplayName("POST /system/roles/create：角色编码含非法字符 → 999901 参数校验失败")
    void create_invalidCode_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "name", "角色",
                        "code", "bad-code",
                        "type", "GLOBAL"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }

    @Test
    @DisplayName("POST /system/roles/create：角色编码已存在 → 透出业务码 010302")
    void create_codeExists_propagatesBusinessCode() throws Exception {
        doThrow(new BusinessException("010302", "角色编码已存在"))
                .when(roleService)
                .create(any(CreateRoleDTO.class));

        mockMvc.perform(post(BASE + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "name", "角色",
                        "code", "DATA_DEVELOPER",
                        "type", "GLOBAL"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("010302"));
    }

    @Test
    @DisplayName("POST /system/roles/update：合法请求转发服务并返回成功")
    void update_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "id", 2,
                        "name", "数据开发者2",
                        "status", "ENABLED"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        ArgumentCaptor<UpdateRoleDTO> captor = ArgumentCaptor.forClass(UpdateRoleDTO.class);
        verify(roleService).update(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(2L);
        assertThat(captor.getValue().getName()).isEqualTo("数据开发者2");
    }

    @Test
    @DisplayName("POST /system/roles/update：角色 ID 缺失 → 999901 参数校验失败")
    void update_missingId_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("name", "数据开发者"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }

    @Test
    @DisplayName("POST /system/roles/update：内置角色 → 透出业务码 010303")
    void update_builtinRole_propagatesBusinessCode() throws Exception {
        doThrow(new BusinessException("010303", "内置角色不可修改"))
                .when(roleService)
                .update(any(UpdateRoleDTO.class));

        mockMvc.perform(post(BASE + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("id", 1, "name", "超级管理员"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("010303"));
    }

    @Test
    @DisplayName("POST /system/roles/{id}/delete：以路径 ID 调用服务删除")
    void delete_validId_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/2/delete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        verify(roleService).delete(2L);
    }

    @Test
    @DisplayName("POST /system/roles/enable-disable：合法请求按 ID 与状态调用服务")
    void enableDisable_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/enable-disable")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("id", 2, "status", "DISABLED"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        verify(roleService).enableDisable(2L, "DISABLED");
    }

    @Test
    @DisplayName("POST /system/roles/enable-disable：停用内置角色 → 透出业务码 010304")
    void enableDisable_builtinRole_propagatesBusinessCode() throws Exception {
        doThrow(new BusinessException("010304", "内置角色不可停用"))
                .when(roleService)
                .enableDisable(1L, "DISABLED");

        mockMvc.perform(post(BASE + "/enable-disable")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("id", 1, "status", "DISABLED"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("010304"));
    }

    @Test
    @DisplayName("POST /system/roles/configure-permissions：合法请求转发完整 DTO")
    void configurePermissions_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/configure-permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "roleId", 2,
                        "menuIds", List.of(10, 11),
                        "dataScope", "DEPT_AND_CHILDREN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        ArgumentCaptor<RolePermissionDTO> captor = ArgumentCaptor.forClass(RolePermissionDTO.class);
        verify(roleService).configurePermissions(captor.capture());
        assertThat(captor.getValue().getRoleId()).isEqualTo(2L);
        assertThat(captor.getValue().getMenuIds()).containsExactly(10L, 11L);
        assertThat(captor.getValue().getDataScope()).isEqualTo("DEPT_AND_CHILDREN");
    }

    @Test
    @DisplayName("POST /system/roles/configure-permissions：数据权限范围缺失 → 999901 参数校验失败")
    void configurePermissions_missingDataScope_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/configure-permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("roleId", 2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }

    @Test
    @DisplayName("POST /system/roles/assign-users：合法请求按角色与用户列表调用服务")
    void assignUsers_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/assign-users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("targetId", 2, "userIds", List.of(100, 101)))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Long>> userIdsCaptor = ArgumentCaptor.forClass(List.class);
        verify(roleService).assignUsers(eq(2L), userIdsCaptor.capture());
        assertThat(userIdsCaptor.getValue()).containsExactly(100L, 101L);
    }

    @Test
    @DisplayName("POST /system/roles/assign-users：userIds 缺失 → 999901 参数校验失败")
    void assignUsers_missingUserIds_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/assign-users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("targetId", 2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }
}
