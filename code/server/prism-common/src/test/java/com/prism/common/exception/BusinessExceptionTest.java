package com.prism.common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 业务异常 BusinessException 测试
 */
@DisplayName("业务异常 BusinessException")
class BusinessExceptionTest {

	@Test
	@DisplayName("自定义编码构造 - code/message 原样保留")
	void constructor_codeAndMessage_valuesPreserved() {
		// Act
		BusinessException exception = new BusinessException("010101", "名称重复");

		// Assert
		assertThat(exception.getCode()).isEqualTo("010101");
		assertThat(exception.getMessage()).isEqualTo("名称重复");
	}

	@ParameterizedTest(name = "{0} → code={1}")
	@CsvSource({ "SYSTEM_ERROR, 999999", "PARAM_INVALID, 999901", "NOT_FOUND, 999904" })
	@DisplayName("ErrorCode 构造 - code/message 取自枚举")
	void constructor_errorCode_usesEnumValues(String enumName, String expectedCode) {
		// Arrange
		ErrorCode errorCode = ErrorCode.valueOf(enumName);

		// Act
		BusinessException exception = new BusinessException(errorCode);

		// Assert
		assertThat(exception.getCode()).isEqualTo(expectedCode);
		assertThat(exception.getMessage()).isEqualTo(errorCode.getMessage());
	}

	@Test
	@DisplayName("ErrorCode + 明细构造 - 消息为枚举消息拼接明细")
	void constructor_errorCodeWithDetail_messageConcatenated() {
		// Act
		BusinessException exception = new BusinessException(ErrorCode.DATA_DUPLICATE, "编码 DUP-001 已存在");

		// Assert
		assertThat(exception.getCode()).isEqualTo("999905");
		assertThat(exception.getMessage()).isEqualTo("数据重复：编码 DUP-001 已存在");
	}

	@Test
	@DisplayName("异常类型 - 属于 RuntimeException 可被全局处理器捕获")
	void inheritance_anyInstance_isRuntimeException() {
		// Act
		BusinessException exception = new BusinessException(ErrorCode.FORBIDDEN);

		// Assert
		assertThat(exception).isInstanceOf(RuntimeException.class);
	}

}
