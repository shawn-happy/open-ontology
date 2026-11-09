package com.prism.platform.service.impl;

import com.prism.platform.entity.SystemAuditLog;
import com.prism.platform.entity.SystemLoginLog;
import com.prism.platform.mapper.SystemAuditLogMapper;
import com.prism.platform.mapper.SystemLoginLogMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 审计日志写入服务测试（审计日志/登录日志落库与异常吞没）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("审计日志写入服务")
class AuditLogServiceImplTest {

    @Mock
    private SystemAuditLogMapper systemAuditLogMapper;

    @Mock
    private SystemLoginLogMapper systemLoginLogMapper;

    @InjectMocks
    private AuditLogServiceImpl auditLogService;

    @Nested
    @DisplayName("保存审计日志")
    class SaveAuditLog {

        @Test
        @DisplayName("正常保存 - 全字段写入并填充操作时间")
        void saveAuditLog_normal_insertsWithAllFields() {
            auditLogService.saveAuditLog(1L, "admin", "用户管理", "新增用户", "CREATE",
                    "SUCCESS", "127.0.0.1", "req-1", "Mozilla/5.0", "{\"id\":10}");

            ArgumentCaptor<SystemAuditLog> captor = ArgumentCaptor.forClass(SystemAuditLog.class);
            verify(systemAuditLogMapper).insert(captor.capture());
            SystemAuditLog saved = captor.getValue();
            assertThat(saved.getUserId()).isEqualTo(1L);
            assertThat(saved.getUsername()).isEqualTo("admin");
            assertThat(saved.getModule()).isEqualTo("用户管理");
            assertThat(saved.getOperation()).isEqualTo("新增用户");
            assertThat(saved.getType()).isEqualTo("CREATE");
            assertThat(saved.getResult()).isEqualTo("SUCCESS");
            assertThat(saved.getIp()).isEqualTo("127.0.0.1");
            assertThat(saved.getRequestId()).isEqualTo("req-1");
            assertThat(saved.getUserAgent()).isEqualTo("Mozilla/5.0");
            assertThat(saved.getDetail()).isEqualTo("{\"id\":10}");
            assertThat(saved.getOperatedAt()).isNotNull();
        }

        @Test
        @DisplayName("Mapper 落库异常 - 吞掉异常不向上抛出")
        void saveAuditLog_mapperThrows_swallowed() {
            when(systemAuditLogMapper.insert(any(SystemAuditLog.class)))
                    .thenThrow(new RuntimeException("db down"));

            assertThatCode(() -> auditLogService.saveAuditLog(1L, "admin", "用户管理",
                    "新增用户", "CREATE", "SUCCESS", "127.0.0.1", "req-1", "UA", "{}"))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("保存登录日志")
    class SaveLoginLog {

        @Test
        @DisplayName("正常保存 - 全字段写入并填充登录时间")
        void saveLoginLog_normal_insertsWithAllFields() {
            auditLogService.saveLoginLog(2L, "u1", "10.0.0.1", "Chrome",
                    "FAIL", "密码错误", "req-2");

            ArgumentCaptor<SystemLoginLog> captor = ArgumentCaptor.forClass(SystemLoginLog.class);
            verify(systemLoginLogMapper).insert(captor.capture());
            SystemLoginLog saved = captor.getValue();
            assertThat(saved.getUserId()).isEqualTo(2L);
            assertThat(saved.getUsername()).isEqualTo("u1");
            assertThat(saved.getIp()).isEqualTo("10.0.0.1");
            assertThat(saved.getUserAgent()).isEqualTo("Chrome");
            assertThat(saved.getResult()).isEqualTo("FAIL");
            assertThat(saved.getFailReason()).isEqualTo("密码错误");
            assertThat(saved.getRequestId()).isEqualTo("req-2");
            assertThat(saved.getLoginAt()).isNotNull();
        }

        @Test
        @DisplayName("Mapper 落库异常 - 吞掉异常不向上抛出")
        void saveLoginLog_mapperThrows_swallowed() {
            when(systemLoginLogMapper.insert(any(SystemLoginLog.class)))
                    .thenThrow(new RuntimeException("db down"));

            assertThatCode(() -> auditLogService.saveLoginLog(2L, "u1", "10.0.0.1",
                    "Chrome", "SUCCESS", null, "req-2"))
                    .doesNotThrowAnyException();
        }
    }
}
