package com.prism.common.result;

import com.prism.common.context.RequestContextHolder;
import com.prism.common.exception.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 统一响应封装 Result 测试
 */
@DisplayName("统一响应封装 Result")
class ResultTest {

	@AfterEach
	void tearDown() {
		RequestContextHolder.clear();
	}

	@Nested
	@DisplayName("成功响应")
	class Ok {

		@Test
		@DisplayName("携带数据 - 返回 000000 与数据体")
		void ok_withData_returnsSuccessCodeAndData() {
			// Arrange
			RequestContextHolder.setRequestId("req-001");

			// Act
			Result<String> result = Result.ok("payload");

			// Assert
			assertThat(result.getCode()).isEqualTo("000000");
			assertThat(result.getMsg()).isEqualTo("操作成功");
			assertThat(result.getData()).isEqualTo("payload");
			assertThat(result.getRequestId()).isEqualTo("req-001");
		}

		@Test
		@DisplayName("无参调用 - data 为 null")
		void ok_withoutData_returnsNullData() {
			// Act
			Result<Object> result = Result.ok();

			// Assert
			assertThat(result.getCode()).isEqualTo("000000");
			assertThat(result.getData()).isNull();
		}

		@Test
		@DisplayName("上下文无 requestId - requestId 为 null")
		void ok_withoutRequestId_returnsNullRequestId() {
			// Act
			Result<Integer> result = Result.ok(1);

			// Assert
			assertThat(result.getRequestId()).isNull();
		}

	}

	@Nested
	@DisplayName("失败响应")
	class Fail {

		@Test
		@DisplayName("自定义编码与消息 - 原样返回")
		void fail_withCodeAndMsg_returnsGivenValues() {
			// Arrange
			RequestContextHolder.setRequestId("req-002");

			// Act
			Result<Void> result = Result.fail("010101", "名称重复");

			// Assert
			assertThat(result.getCode()).isEqualTo("010101");
			assertThat(result.getMsg()).isEqualTo("名称重复");
			assertThat(result.getData()).isNull();
			assertThat(result.getRequestId()).isEqualTo("req-002");
		}

		@ParameterizedTest(name = "错误码 {0} - 消息取自枚举")
		@CsvSource({ "SYSTEM_ERROR, 999999, 系统异常", "PARAM_INVALID, 999901, 参数校验失败",
				"UNAUTHORIZED, 999902, 未认证", "FORBIDDEN, 999903, 无权限", "NOT_FOUND, 999904, 资源不存在",
				"DATA_DUPLICATE, 999905, 数据重复" })
		@DisplayName("ErrorCode 枚举 - 编码与消息与枚举一致")
		void fail_withErrorCode_usesEnumCodeAndMessage(String enumName, String code, String msg) {
			// Arrange
			ErrorCode errorCode = ErrorCode.valueOf(enumName);

			// Act
			Result<Void> result = Result.fail(errorCode);

			// Assert
			assertThat(result.getCode()).isEqualTo(code);
			assertThat(result.getMsg()).isEqualTo(msg);
		}

	}

	@Test
	@DisplayName("Builder 构建 - 字段完整赋值")
	void builder_allFields_valuesPreserved() {
		// Act
		Result<String> result = Result.<String>builder()
			.code("000000")
			.msg("ok")
			.data("d")
			.requestId("r")
			.build();

		// Assert
		assertThat(result.getCode()).isEqualTo("000000");
		assertThat(result.getMsg()).isEqualTo("ok");
		assertThat(result.getData()).isEqualTo("d");
		assertThat(result.getRequestId()).isEqualTo("r");
	}

}
