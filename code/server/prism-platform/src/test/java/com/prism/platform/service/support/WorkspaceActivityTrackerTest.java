package com.prism.platform.service.support;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.prism.platform.constants.WorkspaceConstants;
import com.prism.platform.mapper.WorkspaceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 空间活跃标记组件测试（SETNX 当日活跃标记 + 每日回写 last_active_at）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("空间活跃标记组件")
class WorkspaceActivityTrackerTest {

    private static final String CODE = "SMART-MFG";
    private static final String ACTIVE_KEY = WorkspaceConstants.REDIS_WS_ACTIVE_PREFIX + CODE;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private WorkspaceMapper workspaceMapper;

    @InjectMocks
    private WorkspaceActivityTracker tracker;

    @BeforeEach
    void setUp() {
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    /**
     * 让 SCAN 游标按顺序吐出指定 key（forEachRemaining 为接口默认方法，需显式驱动）
     */
    @SuppressWarnings("unchecked")
    private void stubScan(String... keys) {
        Cursor<String> cursor = Mockito.mock(Cursor.class);
        doAnswer(invocation -> {
            Consumer<String> action = invocation.getArgument(0);
            for (String key : keys) {
                action.accept(key);
            }
            return null;
        }).when(cursor).forEachRemaining(any());
        when(stringRedisTemplate.scan(any(ScanOptions.class))).thenReturn(cursor);
    }

    // ===== markActive =====

    @Test
    @DisplayName("标记活跃：SETNX 写入当日活跃标记，TTL 24 小时")
    void markActive_writesSetnxWith24hTtl() {
        tracker.markActive(CODE);

        verify(valueOperations).setIfAbsent(ACTIVE_KEY, "1",
                WorkspaceConstants.TTL_WS_ACTIVE_SECONDS, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("标记活跃：Redis 异常时容错降级，不影响主流程")
    void markActive_redisFailure_degradesSilently() {
        when(stringRedisTemplate.opsForValue()).thenThrow(new RuntimeException("redis down"));

        assertThatCode(() -> tracker.markActive(CODE)).doesNotThrowAnyException();
    }

    // ===== flushLastActiveAt =====

    @Test
    @DisplayName("回写活跃时间：SCAN 失败时跳过本次回写")
    void flushLastActiveAt_scanFailure_skipsWriteback() {
        when(stringRedisTemplate.scan(any(ScanOptions.class)))
                .thenThrow(new RuntimeException("scan fail"));

        assertThatCode(() -> tracker.flushLastActiveAt()).doesNotThrowAnyException();
        verify(workspaceMapper, never()).update(any(), any());
    }

    @Test
    @DisplayName("回写活跃时间：无活跃空间时不触发 DB 更新")
    void flushLastActiveAt_noActiveKeys_noWriteback() {
        stubScan();

        tracker.flushLastActiveAt();

        verify(workspaceMapper, never()).update(any(), any());
    }

    @Test
    @DisplayName("回写活跃时间：剥离前缀得到空间编码并批量回写")
    void flushLastActiveAt_writesBackActiveCodes() {
        stubScan(WorkspaceConstants.REDIS_WS_ACTIVE_PREFIX + "WS1",
                WorkspaceConstants.REDIS_WS_ACTIVE_PREFIX + "WS2");

        tracker.flushLastActiveAt();

        verify(workspaceMapper).update(isNull(), any(Wrapper.class));
    }

    @Test
    @DisplayName("回写活跃时间：DB 更新异常时容错降级")
    void flushLastActiveAt_dbFailure_degradesSilently() {
        stubScan(WorkspaceConstants.REDIS_WS_ACTIVE_PREFIX + "WS1");
        doThrow(new RuntimeException("db down"))
                .when(workspaceMapper).update(isNull(), any(Wrapper.class));

        assertThatCode(() -> tracker.flushLastActiveAt()).doesNotThrowAnyException();
    }
}
