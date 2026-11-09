package com.prism.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prism.common.exception.BusinessException;
import com.prism.common.exception.GlobalExceptionHandler;
import com.prism.platform.dto.CreateMenuDTO;
import com.prism.platform.dto.UpdateMenuDTO;
import com.prism.platform.service.MenuService;
import com.prism.platform.vo.MenuTreeVO;
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
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 菜单权限管理控制器 MockMvc 薄壳测试（路由 / 参数绑定与校验 / 树内查找 / 统一响应结构）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("菜单权限管理控制器")
class MenuControllerTest {

    private static final String BASE = "/web/system/menus";

    @Mock
    private MenuService menuService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new MenuController(menuService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    /**
     * 构造两层菜单树：目录(1) → 菜单按钮(2)
     */
    private List<MenuTreeVO> sampleTree() {
        MenuTreeVO child = MenuTreeVO.builder()
                .id(2L)
                .parentId(1L)
                .name("新增用户")
                .type("BUTTON")
                .permission("system:user:create")
                .status("ENABLED")
                .build();
        MenuTreeVO root = MenuTreeVO.builder()
                .id(1L)
                .parentId(0L)
                .name("系统管理")
                .type("DIRECTORY")
                .path("/system")
                .sortOrder(1)
                .status("ENABLED")
                .children(List.of(child))
                .build();
        return List.of(root);
    }

    @Test
    @DisplayName("GET /system/menus/tree：返回菜单树")
    void tree_returnsMenuTree() throws Exception {
        when(menuService.tree()).thenReturn(sampleTree());

        mockMvc.perform(get(BASE + "/tree"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data[0].name").value("系统管理"))
                .andExpect(jsonPath("$.data[0].children[0].permission").value("system:user:create"));

        verify(menuService).tree();
    }

    @Test
    @DisplayName("GET /system/menus/{id}：在树的深层节点中命中详情")
    void getById_foundInNestedTree_returnsNode() throws Exception {
        when(menuService.tree()).thenReturn(sampleTree());

        mockMvc.perform(get(BASE + "/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.type").value("BUTTON"));
    }

    @Test
    @DisplayName("GET /system/menus/{id}：树中不存在该节点 → data 为 null")
    void getById_notFoundInTree_returnsNullData() throws Exception {
        when(menuService.tree()).thenReturn(sampleTree());

        mockMvc.perform(get(BASE + "/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    @DisplayName("POST /system/menus/create：合法请求转发服务并返回成功")
    void create_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "parentId", 1,
                        "name", "用户管理",
                        "type", "MENU",
                        "permission", "system:user:list",
                        "path", "/system/users",
                        "sortOrder", 1))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        ArgumentCaptor<CreateMenuDTO> captor = ArgumentCaptor.forClass(CreateMenuDTO.class);
        verify(menuService).create(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("用户管理");
        assertThat(captor.getValue().getType()).isEqualTo("MENU");
        assertThat(captor.getValue().getPermission()).isEqualTo("system:user:list");
    }

    @Test
    @DisplayName("POST /system/menus/create：节点类型缺失 → 999901 参数校验失败")
    void create_missingType_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("parentId", 1, "name", "用户管理"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }

    @Test
    @DisplayName("POST /system/menus/create：菜单类型层级不合法 → 透出业务码 010403")
    void create_invalidTypeHierarchy_propagatesBusinessCode() throws Exception {
        doThrow(new BusinessException("010403", "菜单类型层级不合法"))
                .when(menuService)
                .create(any(CreateMenuDTO.class));

        mockMvc.perform(post(BASE + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "parentId", 2,
                        "name", "非法目录",
                        "type", "DIRECTORY"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("010403"));
    }

    @Test
    @DisplayName("POST /system/menus/update：合法请求转发服务并返回成功")
    void update_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "id", 2,
                        "name", "创建用户",
                        "sortOrder", 3,
                        "status", "ENABLED"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        ArgumentCaptor<UpdateMenuDTO> captor = ArgumentCaptor.forClass(UpdateMenuDTO.class);
        verify(menuService).update(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(2L);
        assertThat(captor.getValue().getSortOrder()).isEqualTo(3);
    }

    @Test
    @DisplayName("POST /system/menus/update：节点 ID 缺失 → 999901 参数校验失败")
    void update_missingId_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("name", "创建用户"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }

    @Test
    @DisplayName("POST /system/menus/{id}/delete：以路径 ID 调用服务删除")
    void delete_validId_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/2/delete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        verify(menuService).delete(2L);
    }

    @Test
    @DisplayName("POST /system/menus/{id}/delete：存在下级节点 → 透出业务码 010402")
    void delete_hasChildren_propagatesBusinessCode() throws Exception {
        doThrow(new BusinessException("010402", "存在下级节点不可删除")).when(menuService).delete(1L);

        mockMvc.perform(post(BASE + "/1/delete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("010402"));
    }

    @Test
    @DisplayName("POST /system/menus/enable-disable：合法请求按 ID 与状态调用服务")
    void enableDisable_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/enable-disable")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("id", 2, "status", "DISABLED"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        verify(menuService).enableDisable(2L, "DISABLED");
    }

    @Test
    @DisplayName("POST /system/menus/enable-disable：状态非法 → 999901 参数校验失败")
    void enableDisable_invalidStatus_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/enable-disable")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("id", 2, "status", "HIDDEN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }
}
