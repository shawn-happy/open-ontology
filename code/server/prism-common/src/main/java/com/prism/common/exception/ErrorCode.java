package com.prism.common.exception;

import lombok.Getter;

/**
 * 错误码枚举
 * 编码规则：6 位字符串 = 模块编码(2位) + 功能编码(2位) + 错误编码(2位)
 * 成功编码固定为 000000
 */
@Getter
public enum ErrorCode {

    // ===== 通用错误 (99) =====
    SYSTEM_ERROR("999999", "系统异常"),
    PARAM_INVALID("999901", "参数校验失败"),
    UNAUTHORIZED("999902", "未认证"),
    FORBIDDEN("999903", "无权限"),
    NOT_FOUND("999904", "资源不存在"),
    DATA_DUPLICATE("999905", "数据重复");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
