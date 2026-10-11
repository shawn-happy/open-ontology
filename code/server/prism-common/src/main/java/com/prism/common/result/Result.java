package com.prism.common.result;

import com.prism.common.context.RequestContextHolder;
import com.prism.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一 API 响应封装
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "统一 API 响应")
public class Result<T> {

    @Schema(description = "业务编码，成功为 000000", example = "000000")
    private String code;

    @Schema(description = "提示信息", example = "操作成功")
    private String msg;

    @Schema(description = "业务数据")
    private T data;

    @Schema(description = "请求追踪 ID")
    private String requestId;

    // ===== 静态工厂方法 =====

    public static <T> Result<T> ok(T data) {
        return Result.<T>builder()
                .code("000000")
                .msg("操作成功")
                .data(data)
                .requestId(RequestContextHolder.getRequestId())
                .build();
    }

    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static <T> Result<T> fail(String code, String msg) {
        return Result.<T>builder()
                .code(code)
                .msg(msg)
                .requestId(RequestContextHolder.getRequestId())
                .build();
    }

    public static <T> Result<T> fail(ErrorCode errorCode) {
        return fail(errorCode.getCode(), errorCode.getMessage());
    }
}
