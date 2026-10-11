package com.prism.common.exception;

import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class BusinessException extends RuntimeException {

    private final String code;

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }

    public BusinessException(ErrorCode errorCode, String detail) {
        this(errorCode.getCode(), errorCode.getMessage() + "：" + detail);
    }
}
