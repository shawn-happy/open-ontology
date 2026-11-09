package com.prism.platform.service.support;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.prism.platform.constants.WorkspaceConstants;
import com.prism.platform.entity.Workspace;
import com.prism.platform.mapper.WorkspaceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 空间活跃标记组件
 * <p>
 * 空间级请求命中后 SETNX 当日活跃标记（TTL 24h，容错降级不影响主流程）；
 * 每日 23:55 定时任务扫描活跃标记，批量回写 platform_workspace.last_active_at
 * （用于平台概览「30 天活跃空间数」统计）。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WorkspaceActivityTracker {

    private final StringRedisTemplate stringRedisTemplate;
    private final WorkspaceMapper workspaceMapper;

    /**
     * 标记空间当日活跃（SETNX，已存在则跳过）
     */
    public void markActive(String workspaceCode) {
        try {
            stringRedisTemplate.opsForValue().setIfAbsent(
                    WorkspaceConstants.REDIS_WS_ACTIVE_PREFIX + workspaceCode, "1",
                    WorkspaceConstants.TTL_WS_ACTIVE_SECONDS, TimeUnit.SECONDS);
        }
        catch (Exception e) {
            log.warn("写入空间活跃标记失败（忽略）：code={}", workspaceCode, e);
        }
    }

    /**
     * 每日回写活跃空间的 last_active_at
     */
    @Scheduled(cron = "0 55 23 * * *")
    public void flushLastActiveAt() {
        List<String> codes = new ArrayList<>();
        String prefix = WorkspaceConstants.REDIS_WS_ACTIVE_PREFIX;
        try (var cursor = stringRedisTemplate.scan(
                ScanOptions.scanOptions().match(prefix + "*").count(500).build())) {
            cursor.forEachRemaining(key -> codes.add(key.substring(prefix.length())));
        }
        catch (Exception e) {
            log.warn("扫描空间活跃标记失败，跳过本次回写", e);
            return;
        }
        if (codes.isEmpty()) {
            return;
        }
        try {
            workspaceMapper.update(null, new LambdaUpdateWrapper<Workspace>()
                    .in(Workspace::getCode, codes)
                    .set(Workspace::getLastActiveAt, LocalDateTime.now()));
            log.info("回写空间活跃时间完成：{} 个空间", codes.size());
        }
        catch (Exception e) {
            log.warn("回写空间活跃时间失败", e);
        }
    }
}
