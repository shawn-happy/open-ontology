package com.prism.platform.service.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prism.platform.constants.WorkspaceConstants;
import com.prism.platform.entity.Workspace;
import com.prism.platform.vo.WorkspaceOverviewVO;
import com.prism.platform.vo.WorkspaceSimpleVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 工作空间 Redis 缓存支持组件测试（info/accessible/overview/member 读写与容错降级）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("工作空间缓存支持组件")
class WorkspaceCacheSupportTest {

    private static final String CODE = "SMART-MFG";
    private static final Long USER_ID = 20L;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private WorkspaceCacheSupport cacheSupport;

    @BeforeEach
    void setUp() {
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        cacheSupport = new WorkspaceCacheSupport(stringRedisTemplate, objectMapper);
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

    private Workspace workspace() {
        Workspace ws = new Workspace();
        ws.setId(1L);
        ws.setCode(CODE);
        ws.setName("智能制造空间");
        ws.setStatus("ENABLED");
        ws.setOwnerUserId(10L);
        return ws;
    }

    private String infoKey() {
        return WorkspaceConstants.REDIS_WS_INFO_PREFIX + CODE;
    }

    // ===== workspace info =====

    @Test
    @DisplayName("空间信息缓存：命中时反序列化返回")
    void getWorkspaceInfo_hit_returnsDeserialized() throws Exception {
        when(valueOperations.get(infoKey())).thenReturn(objectMapper.writeValueAsString(workspace()));

        Workspace cached = cacheSupport.getWorkspaceInfo(CODE);

        assertThat(cached).isNotNull();
        assertThat(cached.getCode()).isEqualTo(CODE);
        assertThat(cached.getName()).isEqualTo("智能制造空间");
        assertThat(cached.getOwnerUserId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("空间信息缓存：未命中返回 null")
    void getWorkspaceInfo_miss_returnsNull() {
        when(valueOperations.get(infoKey())).thenReturn(null);

        assertThat(cacheSupport.getWorkspaceInfo(CODE)).isNull();
    }

    @Test
    @DisplayName("空间信息缓存：Redis 读取异常时容错返回 null")
    void getWorkspaceInfo_redisFailure_returnsNull() {
        when(valueOperations.get(infoKey())).thenThrow(new RuntimeException("redis down"));

        assertThat(cacheSupport.getWorkspaceInfo(CODE)).isNull();
    }

    @Test
    @DisplayName("空间信息缓存：JSON 损坏时按未命中处理")
    void getWorkspaceInfo_corruptJson_returnsNull() {
        when(valueOperations.get(infoKey())).thenReturn("{not-valid-json");

        assertThat(cacheSupport.getWorkspaceInfo(CODE)).isNull();
    }

    @Test
    @DisplayName("写入空间信息缓存：序列化后带 30 分钟 TTL")
    void putWorkspaceInfo_serializesWith30minTtl() throws Exception {
        cacheSupport.putWorkspaceInfo(workspace());

        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        verify(valueOperations).set(infoKey(), jsonCaptor.capture(),
                WorkspaceConstants.TTL_WS_INFO_SECONDS, TimeUnit.SECONDS);
        Workspace written = objectMapper.readValue(jsonCaptor.getValue(), Workspace.class);
        assertThat(written.getCode()).isEqualTo(CODE);
    }

    @Test
    @DisplayName("写入空间信息缓存：Redis 异常时容错忽略")
    void putWorkspaceInfo_redisFailure_degradesSilently() {
        when(stringRedisTemplate.opsForValue()).thenThrow(new RuntimeException("redis down"));

        assertThatCode(() -> cacheSupport.putWorkspaceInfo(workspace())).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("失效空间信息缓存：删除对应 key")
    void evictWorkspaceInfo_deletesKey() {
        cacheSupport.evictWorkspaceInfo(CODE);

        verify(stringRedisTemplate).delete(infoKey());
    }

    @Test
    @DisplayName("失效空间信息缓存：删除异常时容错忽略（TTL 兜底）")
    void evictWorkspaceInfo_redisFailure_degradesSilently() {
        when(stringRedisTemplate.delete(infoKey())).thenThrow(new RuntimeException("redis down"));

        assertThatCode(() -> cacheSupport.evictWorkspaceInfo(CODE)).doesNotThrowAnyException();
    }

    // ===== accessible =====

    @Test
    @DisplayName("可访问空间缓存：命中时反序列化列表返回")
    void getAccessible_hit_returnsList() throws Exception {
        String key = WorkspaceConstants.REDIS_WS_ACCESSIBLE_PREFIX + USER_ID;
        List<WorkspaceSimpleVO> list = List.of(
                WorkspaceSimpleVO.builder().code(CODE).name("智能制造空间").workspaceRole("OWNER")
                        .build());
        when(valueOperations.get(key)).thenReturn(objectMapper.writeValueAsString(list));

        List<WorkspaceSimpleVO> cached = cacheSupport.getAccessible(USER_ID);

        assertThat(cached).hasSize(1);
        assertThat(cached.get(0).getCode()).isEqualTo(CODE);
        assertThat(cached.get(0).getWorkspaceRole()).isEqualTo("OWNER");
    }

    @Test
    @DisplayName("可访问空间缓存：未命中返回 null")
    void getAccessible_miss_returnsNull() {
        when(valueOperations.get(WorkspaceConstants.REDIS_WS_ACCESSIBLE_PREFIX + USER_ID))
                .thenReturn(null);

        assertThat(cacheSupport.getAccessible(USER_ID)).isNull();
    }

    @Test
    @DisplayName("写入可访问空间缓存：带 10 分钟 TTL")
    void putAccessible_writesJsonWith10minTtl() {
        List<WorkspaceSimpleVO> list = List.of(WorkspaceSimpleVO.builder().code(CODE).build());

        cacheSupport.putAccessible(USER_ID, list);

        verify(valueOperations).set(WorkspaceConstants.REDIS_WS_ACCESSIBLE_PREFIX + USER_ID,
                contains(CODE),
                WorkspaceConstants.TTL_WS_ACCESSIBLE_SECONDS, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("失效单个用户可访问缓存：userId 为 null 时不操作")
    void evictAccessible_nullUserId_noop() {
        cacheSupport.evictAccessible(null);

        verify(stringRedisTemplate, never()).delete(anyString());
    }

    @Test
    @DisplayName("失效单个用户可访问缓存：删除对应 key")
    void evictAccessible_deletesUserKey() {
        cacheSupport.evictAccessible(USER_ID);

        verify(stringRedisTemplate)
                .delete(WorkspaceConstants.REDIS_WS_ACCESSIBLE_PREFIX + USER_ID);
    }

    @Test
    @DisplayName("全量失效可访问缓存：SCAN 命中 key 后批量删除")
    void evictAllAccessible_scanDeletesMatchedKeys() {
        String key1 = WorkspaceConstants.REDIS_WS_ACCESSIBLE_PREFIX + "1";
        String key2 = WorkspaceConstants.REDIS_WS_ACCESSIBLE_PREFIX + "2";
        stubScan(key1, key2);

        cacheSupport.evictAllAccessible();

        verify(stringRedisTemplate).delete(List.of(key1, key2));
    }

    @Test
    @DisplayName("全量失效可访问缓存：SCAN 无命中时不触发删除")
    void evictAllAccessible_noKeys_skipsDelete() {
        stubScan();

        cacheSupport.evictAllAccessible();

        verify(stringRedisTemplate, never()).delete(anyCollection());
    }

    @Test
    @DisplayName("全量失效可访问缓存：SCAN 异常时容错忽略")
    void evictAllAccessible_scanFailure_degradesSilently() {
        when(stringRedisTemplate.scan(any(ScanOptions.class)))
                .thenThrow(new RuntimeException("scan fail"));

        assertThatCode(() -> cacheSupport.evictAllAccessible()).doesNotThrowAnyException();
    }

    // ===== overview =====

    @Test
    @DisplayName("平台概览缓存：命中时反序列化返回")
    void getOverview_hit_returnsDeserialized() throws Exception {
        WorkspaceOverviewVO overview = WorkspaceOverviewVO.builder()
                .totalSpaces(3).totalMembers(25).totalAssets(0).activeSpaces30d(2)
                .build();
        when(valueOperations.get(WorkspaceConstants.REDIS_WS_OVERVIEW_KEY))
                .thenReturn(objectMapper.writeValueAsString(overview));

        WorkspaceOverviewVO cached = cacheSupport.getOverview();

        assertThat(cached).isNotNull();
        assertThat(cached.getTotalSpaces()).isEqualTo(3);
        assertThat(cached.getActiveSpaces30d()).isEqualTo(2);
    }

    @Test
    @DisplayName("平台概览缓存：未命中返回 null")
    void getOverview_miss_returnsNull() {
        when(valueOperations.get(WorkspaceConstants.REDIS_WS_OVERVIEW_KEY)).thenReturn(null);

        assertThat(cacheSupport.getOverview()).isNull();
    }

    @Test
    @DisplayName("写入平台概览缓存：带 60 秒 TTL")
    void putOverview_writesWith60sTtl() {
        WorkspaceOverviewVO overview = WorkspaceOverviewVO.builder().totalSpaces(3).build();

        cacheSupport.putOverview(overview);

        verify(valueOperations).set(WorkspaceConstants.REDIS_WS_OVERVIEW_KEY,
                contains("\"totalSpaces\":3"),
                WorkspaceConstants.TTL_WS_OVERVIEW_SECONDS, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("失效平台概览缓存：删除概览 key")
    void evictOverview_deletesKey() {
        cacheSupport.evictOverview();

        verify(stringRedisTemplate).delete(WorkspaceConstants.REDIS_WS_OVERVIEW_KEY);
    }

    // ===== member =====

    @Test
    @DisplayName("成员判定缓存：原样返回角色字符串")
    void getMemberRole_returnsRawValue() {
        String key = WorkspaceConstants.REDIS_WS_MEMBER_PREFIX + CODE + ":" + USER_ID;
        when(valueOperations.get(key)).thenReturn("OWNER");

        assertThat(cacheSupport.getMemberRole(CODE, USER_ID)).isEqualTo("OWNER");
    }

    @Test
    @DisplayName("写入成员判定缓存：字符串值不走 JSON 序列化，带 10 分钟 TTL")
    void putMemberRole_writesRawStringWithoutJson() {
        String key = WorkspaceConstants.REDIS_WS_MEMBER_PREFIX + CODE + ":" + USER_ID;

        cacheSupport.putMemberRole(CODE, USER_ID, "MAINTAINER");

        verify(valueOperations).set(key, "MAINTAINER",
                WorkspaceConstants.TTL_WS_MEMBER_SECONDS, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("失效成员判定缓存：同时删除成员 key 与该用户可访问缓存")
    void evictMember_deletesMemberAndAccessibleKeys() {
        cacheSupport.evictMember(CODE, USER_ID);

        verify(stringRedisTemplate)
                .delete(WorkspaceConstants.REDIS_WS_MEMBER_PREFIX + CODE + ":" + USER_ID);
        verify(stringRedisTemplate)
                .delete(WorkspaceConstants.REDIS_WS_ACCESSIBLE_PREFIX + USER_ID);
    }

    @Test
    @DisplayName("失效空间全部成员缓存：SCAN 前缀命中后批量删除")
    void evictWorkspaceMembers_scanDeletesMatchedKeys() {
        String key1 = WorkspaceConstants.REDIS_WS_MEMBER_PREFIX + CODE + ":1";
        String key2 = WorkspaceConstants.REDIS_WS_MEMBER_PREFIX + CODE + ":2";
        stubScan(key1, key2);

        cacheSupport.evictWorkspaceMembers(CODE);

        verify(stringRedisTemplate).delete(List.of(key1, key2));
    }
}
