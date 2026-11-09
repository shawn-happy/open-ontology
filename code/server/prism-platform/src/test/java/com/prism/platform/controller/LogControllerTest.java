package com.prism.platform.controller;

import com.prism.common.exception.BusinessException;
import com.prism.common.exception.GlobalExceptionHandler;
import com.prism.common.result.PageResult;
import com.prism.platform.dto.AuditLogQuery;
import com.prism.platform.dto.ExecLogQuery;
import com.prism.platform.dto.LoginLogQuery;
import com.prism.platform.service.LogService;
import com.prism.platform.vo.AuditLogVO;
import com.prism.platform.vo.ExecLogVO;
import com.prism.platform.vo.LoginLogVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 日志管理控制器 MockMvc 薄壳测试（路由 / 分页参数绑定 / 统一响应结构）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("日志管理控制器")
class LogControllerTest {

    private static final String BASE = "/web/system/logs";

    @Mock
    private LogService logService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new LogController(logService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /system/logs/audit：分页与筛选条件正确绑定，返回 PageResult 结构")
    void pageAuditLogs_bindsQueryAndReturnsPageResult() throws Exception {
        AuditLogVO vo = AuditLogVO.builder()
                .id(1L)
                .username("admin")
                .module("USER")
                .operation("新增用户")
                .type("CREATE")
                .result("SUCCESS")
                .build();
        PageResult<AuditLogVO> page = PageResult.<AuditLogVO>builder()
                .data(List.of(vo))
                .pageInfo(PageResult.PageInfo.builder()
                        .currentPage(1)
                        .pageSize(20)
                        .total(1L)
                        .totalPages(1)
                        .build())
                .build();
        when(logService.pageAuditLogs(any(AuditLogQuery.class))).thenReturn(page);

        mockMvc.perform(get(BASE + "/audit")
                .param("currentPage", "1")
                .param("pageSize", "20")
                .param("module", "USER")
                .param("type", "CREATE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data.data[0].module").value("USER"))
                .andExpect(jsonPath("$.data.pageInfo.total").value(1));

        ArgumentCaptor<AuditLogQuery> captor = ArgumentCaptor.forClass(AuditLogQuery.class);
        verify(logService).pageAuditLogs(captor.capture());
        AuditLogQuery query = captor.getValue();
        assertThat(query.getCurrentPage()).isEqualTo(1);
        assertThat(query.getModule()).isEqualTo("USER");
        assertThat(query.getType()).isEqualTo("CREATE");
    }

    @Test
    @DisplayName("GET /system/logs/login：分页与筛选条件正确绑定，返回 PageResult 结构")
    void pageLoginLogs_bindsQueryAndReturnsPageResult() throws Exception {
        LoginLogVO vo = LoginLogVO.builder()
                .id(2L)
                .username("zhangsan")
                .ip("10.0.0.1")
                .result("SUCCESS")
                .build();
        PageResult<LoginLogVO> page = PageResult.<LoginLogVO>builder()
                .data(List.of(vo))
                .pageInfo(PageResult.PageInfo.builder()
                        .currentPage(1)
                        .pageSize(20)
                        .total(1L)
                        .totalPages(1)
                        .build())
                .build();
        when(logService.pageLoginLogs(any(LoginLogQuery.class))).thenReturn(page);

        mockMvc.perform(get(BASE + "/login")
                .param("username", "zhangsan")
                .param("result", "SUCCESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data.data[0].username").value("zhangsan"))
                .andExpect(jsonPath("$.data.data[0].result").value("SUCCESS"));

        ArgumentCaptor<LoginLogQuery> captor = ArgumentCaptor.forClass(LoginLogQuery.class);
        verify(logService).pageLoginLogs(captor.capture());
        assertThat(captor.getValue().getUsername()).isEqualTo("zhangsan");
        assertThat(captor.getValue().getResult()).isEqualTo("SUCCESS");
    }

    @Test
    @DisplayName("GET /system/logs/exec：分页与筛选条件正确绑定，返回 PageResult 结构")
    void pageExecLogs_bindsQueryAndReturnsPageResult() throws Exception {
        ExecLogVO vo = ExecLogVO.builder()
                .id(3L)
                .execType("PIPELINE")
                .taskName("每日同步")
                .status("SUCCESS")
                .durationMs(1200L)
                .build();
        PageResult<ExecLogVO> page = PageResult.<ExecLogVO>builder()
                .data(List.of(vo))
                .pageInfo(PageResult.PageInfo.builder()
                        .currentPage(1)
                        .pageSize(20)
                        .total(1L)
                        .totalPages(1)
                        .build())
                .build();
        when(logService.pageExecLogs(any(ExecLogQuery.class))).thenReturn(page);

        mockMvc.perform(get(BASE + "/exec")
                .param("execType", "PIPELINE")
                .param("status", "SUCCESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data.data[0].execType").value("PIPELINE"))
                .andExpect(jsonPath("$.data.data[0].durationMs").value(1200));

        ArgumentCaptor<ExecLogQuery> captor = ArgumentCaptor.forClass(ExecLogQuery.class);
        verify(logService).pageExecLogs(captor.capture());
        assertThat(captor.getValue().getExecType()).isEqualTo("PIPELINE");
        assertThat(captor.getValue().getStatus()).isEqualTo("SUCCESS");
    }

    @Test
    @DisplayName("GET /system/logs/export：按查询条件返回导出列表")
    void exportLogs_bindsQueryAndReturnsList() throws Exception {
        when(logService.exportLogs(any(AuditLogQuery.class))).thenReturn(List.of(AuditLogVO.builder()
                .id(1L)
                .username("admin")
                .module("USER")
                .build()));

        mockMvc.perform(get(BASE + "/export").param("module", "USER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data[0].module").value("USER"));

        ArgumentCaptor<AuditLogQuery> captor = ArgumentCaptor.forClass(AuditLogQuery.class);
        verify(logService).exportLogs(captor.capture());
        assertThat(captor.getValue().getModule()).isEqualTo("USER");
    }

    @Test
    @DisplayName("GET /system/logs/export：导出失败 → 透出业务码 010601")
    void exportLogs_serviceThrows_propagatesBusinessCode() throws Exception {
        when(logService.exportLogs(any(AuditLogQuery.class)))
                .thenThrow(new BusinessException("010601", "日志导出失败"));

        mockMvc.perform(get(BASE + "/export"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("010601"));
    }
}
