package com.prism.common.context;

/**
 * 请求上下文（ThreadLocal）
 * 存储：requestId、workspaceCode、userId
 */
public class RequestContextHolder {

    private static final ThreadLocal<String> REQUEST_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> WORKSPACE_CODE = new ThreadLocal<>();
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    public static void setRequestId(String requestId) {
        REQUEST_ID.set(requestId);
    }

    public static String getRequestId() {
        return REQUEST_ID.get();
    }

    public static void setWorkspaceCode(String workspaceCode) {
        WORKSPACE_CODE.set(workspaceCode);
    }

    public static String getWorkspaceCode() {
        return WORKSPACE_CODE.get();
    }

    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    public static Long getUserId() {
        return USER_ID.get();
    }

    public static void clear() {
        REQUEST_ID.remove();
        WORKSPACE_CODE.remove();
        USER_ID.remove();
    }
}
