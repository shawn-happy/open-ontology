package com.prism.common.aspect;

/**
 * 审计日志写入接口（由业务模块实现）
 */
public interface AuditLogService {

    /**
     * 异步写入审计日志
     *
     * @param userId    操作者 ID
     * @param username  操作者用户名
     * @param module    模块名
     * @param operation 操作内容
     * @param type      操作类型
     * @param result    结果：SUCCESS/FAIL
     * @param ip        IP 地址
     * @param requestId 请求 ID
     * @param userAgent User-Agent
     * @param detail    详情（JSON）
     */
    void saveAuditLog(Long userId, String username, String module, String operation,
                      String type, String result, String ip, String requestId,
                      String userAgent, String detail);

    /**
     * 异步写入登录日志
     *
     * @param userId    用户 ID（登录失败时可能为 null）
     * @param username  用户名
     * @param ip        IP 地址
     * @param userAgent User-Agent
     * @param result    结果：SUCCESS/FAIL
     * @param failReason 失败原因
     * @param requestId 请求 ID
     */
    void saveLoginLog(Long userId, String username, String ip, String userAgent,
                      String result, String failReason, String requestId);
}
