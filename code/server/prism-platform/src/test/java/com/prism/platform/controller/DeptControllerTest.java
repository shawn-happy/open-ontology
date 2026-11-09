package com.prism.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prism.common.exception.BusinessException;
import com.prism.common.exception.GlobalExceptionHandler;
import com.prism.platform.dto.CreateDeptDTO;
import com.prism.platform.dto.DeptTreeQuery;
import com.prism.platform.dto.UpdateDeptDTO;
import com.prism.platform.service.DeptService;
import com.prism.platform.vo.DeptTreeVO;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 部门管理控制器 MockMvc 薄壳测试（路由 / 参数绑定与校验 / 树内查找 / 统一响应结构）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("部门管理控制器")
class DeptControllerTest {

    private static final String BASE = "/web/system/depts";

    @Mock
    private DeptService deptService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new DeptController(deptService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    /**
     * 构造两层部门树：根节点(1) → 子节点(2)
     */
    private List<DeptTreeVO> sampleTree() {
        DeptTreeVO child = DeptTreeVO.builder()
                .id(2L)
                .parentId(1L)
                .name("研发部")
                .code("RD")
                .depth(2)
                .status("ENABLED")
                .build();
        DeptTreeVO root = DeptTreeVO.builder()
                .id(1L)
                .parentId(0L)
                .name("总公司")
                .code("HQ")
                .depth(1)
                .status("ENABLED")
                .children(List.of(child))
                .build();
        return List.of(root);
    }

    @Test
    @DisplayName("GET /system/depts/tree：状态筛选绑定并返回部门树")
    void tree_bindsQueryAndReturnsTree() throws Exception {
        when(deptService.tree(any(DeptTreeQuery.class))).thenReturn(sampleTree());

        mockMvc.perform(get(BASE + "/tree").param("status", "ENABLED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data[0].code").value("HQ"))
                .andExpect(jsonPath("$.data[0].children[0].code").value("RD"));

        ArgumentCaptor<DeptTreeQuery> captor = ArgumentCaptor.forClass(DeptTreeQuery.class);
        verify(deptService).tree(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo("ENABLED");
    }

    @Test
    @DisplayName("GET /system/depts/{id}：在树的深层节点中命中详情")
    void getById_foundInNestedTree_returnsNode() throws Exception {
        when(deptService.tree(any(DeptTreeQuery.class))).thenReturn(sampleTree());

        mockMvc.perform(get(BASE + "/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.name").value("研发部"));
    }

    @Test
    @DisplayName("GET /system/depts/{id}：树中不存在该部门 → data 为 null")
    void getById_notFoundInTree_returnsNullData() throws Exception {
        when(deptService.tree(any(DeptTreeQuery.class))).thenReturn(sampleTree());

        mockMvc.perform(get(BASE + "/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    @DisplayName("POST /system/depts/create：合法请求转发服务并返回成功")
    void create_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "parentId", 1,
                        "name", "测试部",
                        "code", "QA",
                        "sortOrder", 10))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        ArgumentCaptor<CreateDeptDTO> captor = ArgumentCaptor.forClass(CreateDeptDTO.class);
        verify(deptService).create(captor.capture());
        assertThat(captor.getValue().getParentId()).isEqualTo(1L);
        assertThat(captor.getValue().getCode()).isEqualTo("QA");
        assertThat(captor.getValue().getSortOrder()).isEqualTo(10);
    }

    @Test
    @DisplayName("POST /system/depts/create：部门编码含非法字符 → 999901 参数校验失败")
    void create_invalidCode_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "parentId", 1,
                        "name", "测试部",
                        "code", "bad-code"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }

    @Test
    @DisplayName("POST /system/depts/create：部门编码已存在 → 透出业务码 010502")
    void create_codeExists_propagatesBusinessCode() throws Exception {
        doThrow(new BusinessException("010502", "部门编码已存在"))
                .when(deptService)
                .create(any(CreateDeptDTO.class));

        mockMvc.perform(post(BASE + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "parentId", 1,
                        "name", "测试部",
                        "code", "QA"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("010502"));
    }

    @Test
    @DisplayName("POST /system/depts/update：合法请求转发服务并返回成功")
    void update_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "id", 2,
                        "name", "研发二部",
                        "status", "ENABLED"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        ArgumentCaptor<UpdateDeptDTO> captor = ArgumentCaptor.forClass(UpdateDeptDTO.class);
        verify(deptService).update(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(2L);
        assertThat(captor.getValue().getName()).isEqualTo("研发二部");
    }

    @Test
    @DisplayName("POST /system/depts/update：部门 ID 缺失 → 999901 参数校验失败")
    void update_missingId_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("name", "研发二部"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }

    @Test
    @DisplayName("POST /system/depts/{id}/delete：以路径 ID 调用服务删除")
    void delete_validId_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/2/delete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        verify(deptService).delete(2L);
    }

    @Test
    @DisplayName("POST /system/depts/{id}/delete：根节点 → 透出业务码 010503")
    void delete_rootNode_propagatesBusinessCode() throws Exception {
        doThrow(new BusinessException("010503", "根节点不可操作")).when(deptService).delete(0L);

        mockMvc.perform(post(BASE + "/0/delete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("010503"));
    }

    @Test
    @DisplayName("POST /system/depts/enable-disable：合法请求按 ID 与状态调用服务")
    void enableDisable_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/enable-disable")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("id", 2, "status", "DISABLED"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        verify(deptService).enableDisable(2L, "DISABLED");
    }

    @Test
    @DisplayName("POST /system/depts/enable-disable：状态非法 → 999901 参数校验失败")
    void enableDisable_invalidStatus_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/enable-disable")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("id", 2, "status", "PAUSED"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }

    @Test
    @DisplayName("POST /system/depts/assign-users：合法请求按部门与用户列表调用服务")
    void assignUsers_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/assign-users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("targetId", 2, "userIds", List.of(100, 101)))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Long>> userIdsCaptor = ArgumentCaptor.forClass(List.class);
        verify(deptService).assignUsers(eq(2L), userIdsCaptor.capture());
        assertThat(userIdsCaptor.getValue()).containsExactly(100L, 101L);
    }

    @Test
    @DisplayName("POST /system/depts/set-leader：合法请求按部门与负责人调用服务")
    void setLeader_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/set-leader")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("deptId", 2, "leaderId", 100))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        verify(deptService).setLeader(2L, 100L);
    }

    @Test
    @DisplayName("POST /system/depts/set-leader：负责人缺失 → 999901 参数校验失败")
    void setLeader_missingLeaderId_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/set-leader")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("deptId", 2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }

    @Test
    @DisplayName("POST /system/depts/transfer：合法请求按源与目标部门调用服务")
    void transfer_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("sourceDeptId", 2, "targetDeptId", 3))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        verify(deptService).transfer(2L, 3L);
    }

    @Test
    @DisplayName("POST /system/depts/transfer：目标为自身下级 → 透出业务码 010504")
    void transfer_targetIsChild_propagatesBusinessCode() throws Exception {
        doThrow(new BusinessException("010504", "上级部门不可为自身或下级"))
                .when(deptService)
                .transfer(1L, 2L);

        mockMvc.perform(post(BASE + "/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("sourceDeptId", 1, "targetDeptId", 2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("010504"));
    }

    @Test
    @DisplayName("POST /system/depts/update-sort：合法请求按 ID 与排序值调用服务")
    void updateSort_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/update-sort")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("id", 2, "sortOrder", 5))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        verify(deptService).updateSort(2L, 5);
    }

    @Test
    @DisplayName("POST /system/depts/update-sort：排序值缺失 → 999901 参数校验失败")
    void updateSort_missingSortOrder_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/update-sort")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("id", 2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }
}
