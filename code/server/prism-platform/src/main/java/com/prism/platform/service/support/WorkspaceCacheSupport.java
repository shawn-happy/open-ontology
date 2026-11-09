package com.prism.platform.service.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prism.platform.constants.WorkspaceConstants;
import com.prism.platform.entity.Workspace;
import com.prism.platform.vo.WorkspaceOverviewVO;
import com.prism.platform.vo.WorkspaceSimpleVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 工作空间 Redis 缓存读写与失效支持组件
 * <p>
 * Key 规划（技术设计 §7.3）：info 30min / accessible 10min / member 10min / overview 60s / active 24h。
 * 缓存操作全部容错降级：Redis 异常时回源 DB，不影响主流程。
 * 列表 / 分页 / 统计接口不走缓存（PRD 同步延迟 0）。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WorkspaceCacheSupport {

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    // ===== 空间信息 workspace:info:{code} =====

    public Workspace getWorkspaceInfo(String code) {
        String json = get(WorkspaceConstants.REDIS_WS_INFO_PREFIX + code);
        if (json == null) {
            return null;
        }
        return read(json, Workspace.class);
    }

    public void putWorkspaceInfo(Workspace workspace) {
        put(WorkspaceConstants.REDIS_WS_INFO_PREFIX + workspace.getCode(), workspace,
                WorkspaceConstants.TTL_WS_INFO_SECONDS);
    }

    public void evictWorkspaceInfo(String code) {
        delete(WorkspaceConstants.REDIS_WS_INFO_PREFIX + code);
    }

    // ===== 可访问空间 workspace:accessible:{userId} =====

    public List<WorkspaceSimpleVO> getAccessible(Long userId) {
        String json = get(WorkspaceConstants.REDIS_WS_ACCESSIBLE_PREFIX + userId);
        if (json == null) {
            return null;
        }
        return read(json, new TypeReference<List<WorkspaceSimpleVO>>() {
        });
    }

    public void putAccessible(Long userId, List<WorkspaceSimpleVO> list) {
        put(WorkspaceConstants.REDIS_WS_ACCESSIBLE_PREFIX + userId, list,
                WorkspaceConstants.TTL_WS_ACCESSIBLE_SECONDS);
    }

    public void evictAccessible(Long userId) {
        if (userId != null) {
            delete(WorkspaceConstants.REDIS_WS_ACCESSIBLE_PREFIX + userId);
        }
    }

    /**
     * 全量失效可访问空间缓存（空间启停用 / 删除时，SCAN 前缀清理，禁用 keys()）
     */
    public void evictAllAccessible() {
        scanDelete(WorkspaceConstants.REDIS_WS_ACCESSIBLE_PREFIX + "*");
    }

    // ===== 平台概览 workspace:overview =====

    public WorkspaceOverviewVO getOverview() {
        String json = get(WorkspaceConstants.REDIS_WS_OVERVIEW_KEY);
        if (json == null) {
            return null;
        }
        return read(json, WorkspaceOverviewVO.class);
    }

    public void putOverview(WorkspaceOverviewVO overview) {
        put(WorkspaceConstants.REDIS_WS_OVERVIEW_KEY, overview, WorkspaceConstants.TTL_WS_OVERVIEW_SECONDS);
    }

    public void evictOverview() {
        delete(WorkspaceConstants.REDIS_WS_OVERVIEW_KEY);
    }

    // ===== 成员判定 workspace:member:{code}:{userId} =====

    public String getMemberRole(String code, Long userId) {
        return get(WorkspaceConstants.REDIS_WS_MEMBER_PREFIX + code + ":" + userId);
    }

    public void putMemberRole(String code, Long userId, String value) {
        put(WorkspaceConstants.REDIS_WS_MEMBER_PREFIX + code + ":" + userId, value,
                WorkspaceConstants.TTL_WS_MEMBER_SECONDS);
    }

    public void evictMember(String code, Long userId) {
        delete(WorkspaceConstants.REDIS_WS_MEMBER_PREFIX + code + ":" + userId);
        evictAccessible(userId);
    }

    /**
     * 失效空间全部成员判定缓存（部门维度变更，SCAN 前缀清理）
     */
    public void evictWorkspaceMembers(String code) {
        scanDelete(WorkspaceConstants.REDIS_WS_MEMBER_PREFIX + code + ":*");
    }

    // ===== 基础操作（容错降级） =====

    private String get(String key) {
        try {
            return stringRedisTemplate.opsForValue().get(key);
        }
        catch (Exception e) {
            log.warn("读取缓存失败，回源 DB：key={}", key, e);
            return null;
        }
    }

    private void put(String key, Object value, long ttlSeconds) {
        try {
            String json = value instanceof String str ? str : objectMapper.writeValueAsString(value);
            stringRedisTemplate.opsForValue().set(key, json, ttlSeconds, TimeUnit.SECONDS);
        }
        catch (Exception e) {
            log.warn("写入缓存失败（忽略）：key={}", key, e);
        }
    }

    private void delete(String key) {
        try {
            stringRedisTemplate.delete(key);
        }
        catch (Exception e) {
            log.warn("删除缓存失败（TTL 兜底）：key={}", key, e);
        }
    }

    private void scanDelete(String pattern) {
        try (var cursor = stringRedisTemplate.scan(
                ScanOptions.scanOptions().match(pattern).count(500).build())) {
            List<String> keys = new ArrayList<>();
            cursor.forEachRemaining(keys::add);
            if (!keys.isEmpty()) {
                stringRedisTemplate.delete(keys);
            }
        }
        catch (Exception e) {
            log.warn("SCAN 清理缓存失败（TTL 兜底）：pattern={}", pattern, e);
        }
    }

    private <T> T read(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        }
        catch (Exception e) {
            log.warn("反序列化缓存失败，按未命中处理", e);
            return null;
        }
    }

    private <T> T read(String json, TypeReference<T> type) {
        try {
            return objectMapper.readValue(json, type);
        }
        catch (Exception e) {
            log.warn("反序列化缓存失败，按未命中处理", e);
            return null;
        }
    }
}
