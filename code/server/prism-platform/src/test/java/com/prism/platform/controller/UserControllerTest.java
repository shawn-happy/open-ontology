package com.prism.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prism.common.exception.BusinessException;
import com.prism.common.exception.GlobalExceptionHandler;
import com.prism.common.result.PageResult;
import com.prism.platform.dto.CreateUserDTO;
import com.prism.platform.dto.UpdateUserDTO;
import com.prism.platform.dto.UserPageQuery;
import com.prism.platform.service.UserService;
import com.prism.platform.vo.ImportResultVO;
import com.prism.platform.vo.UserDetailVO;
import com.prism.platform.vo.UserListVO;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 用户管理控制器 MockMvc 薄壳测试（路由 / 参数绑定与校验 / 统一响应结构）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("用户管理控制器")
class UserControllerTest {

    private static final String BASE = "/web/system/users";

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new UserController(userService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /system/users：分页与筛选条件正确绑定，返回 PageResult 结构")
    void page_bindsQueryAndReturnsPageResult() throws Exception {
        UserListVO vo = UserListVO.builder()
                .id(100L)
                .username("zhangsan")
                .name("张三")
                .status("ENABLED")
                .deptId(3L)
                .deptName("研发部")
                .build();
        PageResult<UserListVO> page = PageResult.<UserListVO>builder()
                .data(List.of(vo))
                .pageInfo(PageResult.PageInfo.builder()
                        .currentPage(2)
                        .pageSize(10)
                        .total(11L)
                        .totalPages(2)
                        .build())
                .build();
        when(userService.page(any(UserPageQuery.class))).thenReturn(page);

        mockMvc.perform(get(BASE)
                .param("currentPage", "2")
                .param("pageSize", "10")
                .param("keyword", "zhang")
                .param("deptId", "3")
                .param("status", "ENABLED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data.data[0].username").value("zhangsan"))
                .andExpect(jsonPath("$.data.pageInfo.total").value(11));

        ArgumentCaptor<UserPageQuery> captor = ArgumentCaptor.forClass(UserPageQuery.class);
        verify(userService).page(captor.capture());
        UserPageQuery query = captor.getValue();
        assertThat(query.getCurrentPage()).isEqualTo(2);
        assertThat(query.getPageSize()).isEqualTo(10);
        assertThat(query.getKeyword()).isEqualTo("zhang");
        assertThat(query.getDeptId()).isEqualTo(3L);
        assertThat(query.getStatus()).isEqualTo("ENABLED");
    }

    @Test
    @DisplayName("GET /system/users/{id}：返回用户详情")
    void getById_whenExists_returnsDetail() throws Exception {
        when(userService.getById(100L)).thenReturn(UserDetailVO.builder()
                .id(100L)
                .username("zhangsan")
                .name("张三")
                .status("ENABLED")
                .build());

        mockMvc.perform(get(BASE + "/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data.id").value(100))
                .andExpect(jsonPath("$.data.name").value("张三"));

        verify(userService).getById(100L);
    }

    @Test
    @DisplayName("POST /system/users/create：合法请求转发服务并返回成功")
    void create_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "username", "lisi",
                        "name", "李四",
                        "phone", "13800138000",
                        "deptId", 3,
                        "status", "ENABLED"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        ArgumentCaptor<CreateUserDTO> captor = ArgumentCaptor.forClass(CreateUserDTO.class);
        verify(userService).create(captor.capture());
        assertThat(captor.getValue().getUsername()).isEqualTo("lisi");
        assertThat(captor.getValue().getDeptId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("POST /system/users/create：用户名含非法字符 → 999901 参数校验失败")
    void create_invalidUsername_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "username", "bad user!",
                        "name", "李四",
                        "status", "ENABLED"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }

    @Test
    @DisplayName("POST /system/users/create：用户名已存在 → 透出业务码 010201")
    void create_usernameExists_propagatesBusinessCode() throws Exception {
        doThrow(new BusinessException("010201", "用户名已存在"))
                .when(userService)
                .create(any(CreateUserDTO.class));

        mockMvc.perform(post(BASE + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "username", "lisi",
                        "name", "李四",
                        "status", "ENABLED"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("010201"));
    }

    @Test
    @DisplayName("POST /system/users/update：合法请求转发服务并返回成功")
    void update_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "id", 100,
                        "name", "张三丰",
                        "email", "zsf@example.com",
                        "status", "ENABLED"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        ArgumentCaptor<UpdateUserDTO> captor = ArgumentCaptor.forClass(UpdateUserDTO.class);
        verify(userService).update(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(100L);
        assertThat(captor.getValue().getName()).isEqualTo("张三丰");
    }

    @Test
    @DisplayName("POST /system/users/update：用户 ID 缺失 → 999901 参数校验失败")
    void update_missingId_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("name", "张三", "status", "ENABLED"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }

    @Test
    @DisplayName("POST /system/users/{id}/delete：以路径 ID 调用服务删除")
    void delete_validId_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/100/delete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        verify(userService).delete(100L);
    }

    @Test
    @DisplayName("POST /system/users/enable-disable：合法请求按 ID 与状态调用服务")
    void enableDisable_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/enable-disable")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("id", 100, "status", "DISABLED"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        verify(userService).enableDisable(100L, "DISABLED");
    }

    @Test
    @DisplayName("POST /system/users/enable-disable：状态非法 → 999901 参数校验失败")
    void enableDisable_invalidStatus_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/enable-disable")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("id", 100, "status", "FROZEN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }

    @Test
    @DisplayName("POST /system/users/enable-disable：停用 admin → 透出业务码 010204")
    void enableDisable_adminAccount_propagatesBusinessCode() throws Exception {
        doThrow(new BusinessException("010204", "admin 账号不可停用"))
                .when(userService)
                .enableDisable(1L, "DISABLED");

        mockMvc.perform(post(BASE + "/enable-disable")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("id", 1, "status", "DISABLED"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("010204"));
    }

    @Test
    @DisplayName("POST /system/users/{id}/reset-password：以路径 ID 调用服务重置密码")
    void resetPassword_validId_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/100/reset-password"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        verify(userService).resetPassword(100L);
    }

    @Test
    @DisplayName("POST /system/users/assign-roles：合法请求按用户与角色列表调用服务")
    void assignRoles_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/assign-roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("userId", 100, "roleIds", List.of(2, 3)))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Long>> roleIdsCaptor = ArgumentCaptor.forClass(List.class);
        verify(userService).assignRoles(eq(100L), roleIdsCaptor.capture());
        assertThat(roleIdsCaptor.getValue()).containsExactly(2L, 3L);
    }

    @Test
    @DisplayName("POST /system/users/assign-roles：roleIds 缺失 → 999901 参数校验失败")
    void assignRoles_missingRoleIds_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/assign-roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("userId", 100))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }

    @Test
    @DisplayName("POST /system/users/assign-dept：合法请求按用户与部门调用服务")
    void assignDept_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/assign-dept")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("userId", 100, "deptId", 3))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        verify(userService).assignDept(100L, 3L);
    }

    @Test
    @DisplayName("POST /system/users/import：上传文件返回导入结果统计")
    void importUsers_validFile_returnsImportResult() throws Exception {
        when(userService.importUsers(any(MultipartFile.class))).thenReturn(ImportResultVO.builder()
                .totalCount(3)
                .successCount(2)
                .failCount(1)
                .failDetails(List.of(ImportResultVO.FailDetail.builder()
                        .rowNum(3)
                        .username("wangwu")
                        .reason("用户名已存在")
                        .build()))
                .build());
        MockMultipartFile file = new MockMultipartFile(
                "file", "users.csv", "text/csv", "username,name".getBytes());

        mockMvc.perform(multipart(BASE + "/import").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data.totalCount").value(3))
                .andExpect(jsonPath("$.data.successCount").value(2))
                .andExpect(jsonPath("$.data.failDetails[0].reason").value("用户名已存在"));
    }

    @Test
    @DisplayName("GET /system/users/export：按查询条件返回导出列表")
    void exportUsers_bindsQueryAndReturnsList() throws Exception {
        when(userService.exportUsers(any(UserPageQuery.class)))
                .thenReturn(List.of(UserListVO.builder()
                        .id(100L)
                        .username("zhangsan")
                        .name("张三")
                        .build()));

        mockMvc.perform(get(BASE + "/export").param("status", "ENABLED").param("deptId", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data[0].username").value("zhangsan"));

        ArgumentCaptor<UserPageQuery> captor = ArgumentCaptor.forClass(UserPageQuery.class);
        verify(userService).exportUsers(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo("ENABLED");
        assertThat(captor.getValue().getDeptId()).isEqualTo(3L);
    }
}
