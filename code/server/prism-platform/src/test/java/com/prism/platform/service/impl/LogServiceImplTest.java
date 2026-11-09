package com.prism.platform.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prism.common.result.PageResult;
import com.prism.platform.dto.AuditLogQuery;
import com.prism.platform.dto.ExecLogQuery;
import com.prism.platform.dto.LoginLogQuery;
import com.prism.platform.entity.SystemAuditLog;
import com.prism.platform.entity.SystemExecLog;
import com.prism.platform.entity.SystemLoginLog;
import com.prism.platform.mapper.SystemAuditLogMapper;
import com.prism.platform.mapper.SystemExecLogMapper;
import com.prism.platform.mapper.SystemLoginLogMapper;
import com.prism.platform.vo.AuditLogVO;
import com.prism.platform.vo.ExecLogVO;
import com.prism.platform.vo.LoginLogVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 日志管理服务测试（审计日志/登录日志/执行日志分页查询与导出）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("日志管理服务")
class LogServiceImplTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 11, 10, 12, 0, 0);

    @Mock
    private SystemAuditLogMapper systemAuditLogMapper;

    @Mock
    private SystemLoginLogMapper systemLoginLogMapper;

    @Mock
    private SystemExecLogMapper systemExecLogMapper;

    @InjectMocks
    private LogServiceImpl logService;

    private SystemAuditLog auditLog() {
        SystemAuditLog log = new SystemAuditLog();
        log.setId(1L);
        log.setUserId(2L);
        log.setUsername("admin");
        log.setModule("用户管理");
        log.setOperation("新增用户");
        log.setType("CREATE");
        log.setResult("SUCCESS");
        log.setIp("127.0.0.1");
        log.setRequestId("req-1");
        log.setUserAgent("Mozilla/5.0");
        log.setDetail("{}");
        log.setOperatedAt(NOW);
        return log;
    }

    private SystemLoginLog loginLog() {
        SystemLoginLog log = new SystemLoginLog();
        log.setId(1L);
        log.setUserId(2L);
        log.setUsername("admin");
        log.setIp("10.0.0.1");
        log.setLocation("杭州");
        log.setBrowser("Chrome");
        log.setOs("macOS");
        log.setResult("SUCCESS");
        log.setFailReason(null);
        log.setRequestId("req-2");
        log.setLoginAt(NOW);
        return log;
    }

    private SystemExecLog execLog() {
        SystemExecLog log = new SystemExecLog();
        log.setId(1L);
        log.setExecType("ACTION");
        log.setTaskName("同步用户");
        log.setTriggerUserId(2L);
        log.setTriggerUsername("admin");
        log.setStatus("SUCCESS");
        log.setDurationMs(350L);
        log.setDetail("{}");
        log.setRequestId("req-3");
        log.setStartedAt(NOW);
        log.setFinishedAt(NOW.plusSeconds(1));
        return log;
    }

    @Nested
    @DisplayName("分页查询审计日志")
    class PageAuditLogs {

        @Test
        @DisplayName("带全部筛选条件 - 返回审计日志 VO 列表")
        void pageAuditLogs_withAllFilters_returnsVOs() {
            AuditLogQuery query = new AuditLogQuery();
            query.setKeyword("admin");
            query.setModule("用户管理");
            query.setType("CREATE");
            query.setStartTime(NOW.minusDays(1));
            query.setEndTime(NOW);
            when(systemAuditLogMapper.selectPage(any(), any())).thenAnswer(invocation -> {
                Page<SystemAuditLog> page = invocation.getArgument(0);
                page.setRecords(List.of(auditLog()));
                page.setTotal(1);
                return page;
            });

            PageResult<AuditLogVO> result = logService.pageAuditLogs(query);

            assertThat(result.getData()).hasSize(1);
            AuditLogVO vo = result.getData().get(0);
            assertThat(vo.getId()).isEqualTo(1L);
            assertThat(vo.getUserId()).isEqualTo(2L);
            assertThat(vo.getUsername()).isEqualTo("admin");
            assertThat(vo.getModule()).isEqualTo("用户管理");
            assertThat(vo.getOperation()).isEqualTo("新增用户");
            assertThat(vo.getType()).isEqualTo("CREATE");
            assertThat(vo.getResult()).isEqualTo("SUCCESS");
            assertThat(vo.getIp()).isEqualTo("127.0.0.1");
            assertThat(vo.getRequestId()).isEqualTo("req-1");
            assertThat(vo.getUserAgent()).isEqualTo("Mozilla/5.0");
            assertThat(vo.getDetail()).isEqualTo("{}");
            assertThat(vo.getOperatedAt()).isEqualTo(NOW);
            assertThat(result.getPageInfo().getTotal()).isEqualTo(1);
        }

        @Test
        @DisplayName("无筛选条件 - 正常分页返回空列表")
        void pageAuditLogs_emptyQueryAndNoRecords_returnsEmpty() {
            when(systemAuditLogMapper.selectPage(any(), any())).thenAnswer(invocation -> {
                Page<SystemAuditLog> page = invocation.getArgument(0);
                page.setRecords(Collections.emptyList());
                page.setTotal(0);
                return page;
            });

            PageResult<AuditLogVO> result = logService.pageAuditLogs(new AuditLogQuery());

            assertThat(result.getData()).isEmpty();
            assertThat(result.getPageInfo().getTotal()).isZero();
        }
    }

    @Nested
    @DisplayName("分页查询登录日志")
    class PageLoginLogs {

        @Test
        @DisplayName("带全部筛选条件 - 返回登录日志 VO 列表")
        void pageLoginLogs_withAllFilters_returnsVOs() {
            LoginLogQuery query = new LoginLogQuery();
            query.setUsername("admin");
            query.setResult("SUCCESS");
            query.setStartTime(NOW.minusDays(1));
            query.setEndTime(NOW);
            when(systemLoginLogMapper.selectPage(any(), any())).thenAnswer(invocation -> {
                Page<SystemLoginLog> page = invocation.getArgument(0);
                page.setRecords(List.of(loginLog()));
                page.setTotal(1);
                return page;
            });

            PageResult<LoginLogVO> result = logService.pageLoginLogs(query);

            assertThat(result.getData()).hasSize(1);
            LoginLogVO vo = result.getData().get(0);
            assertThat(vo.getId()).isEqualTo(1L);
            assertThat(vo.getUserId()).isEqualTo(2L);
            assertThat(vo.getUsername()).isEqualTo("admin");
            assertThat(vo.getIp()).isEqualTo("10.0.0.1");
            assertThat(vo.getLocation()).isEqualTo("杭州");
            assertThat(vo.getBrowser()).isEqualTo("Chrome");
            assertThat(vo.getOs()).isEqualTo("macOS");
            assertThat(vo.getResult()).isEqualTo("SUCCESS");
            assertThat(vo.getRequestId()).isEqualTo("req-2");
            assertThat(vo.getLoginAt()).isEqualTo(NOW);
            assertThat(result.getPageInfo().getTotal()).isEqualTo(1);
        }

        @Test
        @DisplayName("无筛选条件 - 正常分页返回空列表")
        void pageLoginLogs_emptyQueryAndNoRecords_returnsEmpty() {
            when(systemLoginLogMapper.selectPage(any(), any())).thenAnswer(invocation -> {
                Page<SystemLoginLog> page = invocation.getArgument(0);
                page.setRecords(Collections.emptyList());
                page.setTotal(0);
                return page;
            });

            PageResult<LoginLogVO> result = logService.pageLoginLogs(new LoginLogQuery());

            assertThat(result.getData()).isEmpty();
        }
    }

    @Nested
    @DisplayName("分页查询执行日志")
    class PageExecLogs {

        @Test
        @DisplayName("带全部筛选条件 - 返回执行日志 VO 列表")
        void pageExecLogs_withAllFilters_returnsVOs() {
            ExecLogQuery query = new ExecLogQuery();
            query.setKeyword("同步");
            query.setExecType("ACTION");
            query.setStatus("SUCCESS");
            query.setStartTime(NOW.minusDays(1));
            query.setEndTime(NOW);
            when(systemExecLogMapper.selectPage(any(), any())).thenAnswer(invocation -> {
                Page<SystemExecLog> page = invocation.getArgument(0);
                page.setRecords(List.of(execLog()));
                page.setTotal(1);
                return page;
            });

            PageResult<ExecLogVO> result = logService.pageExecLogs(query);

            assertThat(result.getData()).hasSize(1);
            ExecLogVO vo = result.getData().get(0);
            assertThat(vo.getId()).isEqualTo(1L);
            assertThat(vo.getExecType()).isEqualTo("ACTION");
            assertThat(vo.getTaskName()).isEqualTo("同步用户");
            assertThat(vo.getTriggerUserId()).isEqualTo(2L);
            assertThat(vo.getTriggerUsername()).isEqualTo("admin");
            assertThat(vo.getStatus()).isEqualTo("SUCCESS");
            assertThat(vo.getDurationMs()).isEqualTo(350L);
            assertThat(vo.getDetail()).isEqualTo("{}");
            assertThat(vo.getRequestId()).isEqualTo("req-3");
            assertThat(vo.getStartedAt()).isEqualTo(NOW);
            assertThat(vo.getFinishedAt()).isEqualTo(NOW.plusSeconds(1));
            assertThat(result.getPageInfo().getTotal()).isEqualTo(1);
        }

        @Test
        @DisplayName("无筛选条件 - 正常分页返回空列表")
        void pageExecLogs_emptyQueryAndNoRecords_returnsEmpty() {
            when(systemExecLogMapper.selectPage(any(), any())).thenAnswer(invocation -> {
                Page<SystemExecLog> page = invocation.getArgument(0);
                page.setRecords(Collections.emptyList());
                page.setTotal(0);
                return page;
            });

            PageResult<ExecLogVO> result = logService.pageExecLogs(new ExecLogQuery());

            assertThat(result.getData()).isEmpty();
        }
    }

    @Nested
    @DisplayName("导出审计日志")
    class ExportLogs {

        @Test
        @DisplayName("带全部筛选条件导出 - 返回 VO 列表")
        void exportLogs_withAllFilters_returnsVOs() {
            AuditLogQuery query = new AuditLogQuery();
            query.setKeyword("admin");
            query.setModule("用户管理");
            query.setType("CREATE");
            query.setStartTime(NOW.minusDays(1));
            query.setEndTime(NOW);
            when(systemAuditLogMapper.selectList(any())).thenReturn(List.of(auditLog()));

            List<AuditLogVO> result = logService.exportLogs(query);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getUsername()).isEqualTo("admin");
            assertThat(result.get(0).getModule()).isEqualTo("用户管理");
            assertThat(result.get(0).getOperatedAt()).isEqualTo(NOW);
            verify(systemAuditLogMapper).selectList(any());
        }

        @Test
        @DisplayName("无筛选条件导出 - 结果为空时返回空列表")
        void exportLogs_emptyQueryAndNoRecords_returnsEmpty() {
            when(systemAuditLogMapper.selectList(any())).thenReturn(Collections.emptyList());

            assertThat(logService.exportLogs(new AuditLogQuery())).isEmpty();
        }
    }
}
