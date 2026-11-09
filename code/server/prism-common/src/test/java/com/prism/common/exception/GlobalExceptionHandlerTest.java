package com.prism.common.exception;

import com.prism.common.context.RequestContextHolder;
import com.prism.common.result.PageQuery;
import com.prism.common.result.Result;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 全局异常处理器 GlobalExceptionHandler 测试
 */
@DisplayName("全局异常处理器 GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

	private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

	@AfterEach
	void tearDown() {
		RequestContextHolder.clear();
	}

	private MethodArgumentNotValidException buildValidationException(FieldError... errors) throws Exception {
		BindingResult bindingResult = new BeanPropertyBindingResult(new PageQuery(), "pageQuery");
		for (FieldError error : errors) {
			bindingResult.addError(error);
		}
		Method method = String.class.getMethod("compareTo", String.class);
		return new MethodArgumentNotValidException(new MethodParameter(method, 0), bindingResult);
	}

	@Nested
	@DisplayName("业务异常处理")
	class HandleBusiness {

		@ParameterizedTest(name = "code={0}, msg={1}")
		@CsvSource({ "010101, 名称重复", "999904, 资源不存在", "020207, 超出可操作等级范围" })
		@DisplayName("自定义编码 - Result 原样携带 code/msg")
		void handleBusiness_customCode_returnsSameCodeAndMsg(String code, String msg) {
			// Arrange
			RequestContextHolder.setRequestId("rid-1");
			BusinessException exception = new BusinessException(code, msg);

			// Act
			Result<Void> result = handler.handleBusiness(exception);

			// Assert
			assertThat(result.getCode()).isEqualTo(code);
			assertThat(result.getMsg()).isEqualTo(msg);
			assertThat(result.getData()).isNull();
			assertThat(result.getRequestId()).isEqualTo("rid-1");
		}

		@Test
		@DisplayName("ErrorCode 构造的异常 - code/msg 取自枚举")
		void handleBusiness_fromErrorCode_usesEnumValues() {
			// Arrange
			BusinessException exception = new BusinessException(ErrorCode.FORBIDDEN);

			// Act
			Result<Void> result = handler.handleBusiness(exception);

			// Assert
			assertThat(result.getCode()).isEqualTo("999903");
			assertThat(result.getMsg()).isEqualTo("无权限");
		}

	}

	@Nested
	@DisplayName("参数校验异常处理")
	class HandleValidation {

		@Test
		@DisplayName("单个字段错误 - 返回 999901 与字段级消息")
		void handleValidation_singleFieldError_returnsParamInvalid() throws Exception {
			// Arrange
			MethodArgumentNotValidException exception = buildValidationException(
					new FieldError("pageQuery", "pageSize", "每页条数最大为 100"));

			// Act
			Result<Void> result = handler.handleValidation(exception);

			// Assert
			assertThat(result.getCode()).isEqualTo(ErrorCode.PARAM_INVALID.getCode());
			assertThat(result.getMsg()).isEqualTo("pageSize: 每页条数最大为 100");
		}

		@Test
		@DisplayName("多个字段错误 - 消息以分号拼接")
		void handleValidation_multipleFieldErrors_joinedBySemicolon() throws Exception {
			// Arrange
			MethodArgumentNotValidException exception = buildValidationException(
					new FieldError("pageQuery", "currentPage", "页码最小为 1"),
					new FieldError("pageQuery", "pageSize", "每页条数最小为 1"));

			// Act
			Result<Void> result = handler.handleValidation(exception);

			// Assert
			assertThat(result.getCode()).isEqualTo("999901");
			assertThat(result.getMsg()).isEqualTo("currentPage: 页码最小为 1; pageSize: 每页条数最小为 1");
		}

		@Test
		@DisplayName("无字段错误 - 消息为空串")
		void handleValidation_noFieldErrors_emptyMsg() throws Exception {
			// Arrange
			MethodArgumentNotValidException exception = buildValidationException();

			// Act
			Result<Void> result = handler.handleValidation(exception);

			// Assert
			assertThat(result.getCode()).isEqualTo("999901");
			assertThat(result.getMsg()).isEmpty();
		}

	}

	@Nested
	@DisplayName("参数绑定异常处理")
	class HandleBind {

		@Test
		@DisplayName("绑定字段错误 - 返回 999901 与字段级消息")
		void handleBind_fieldError_returnsParamInvalid() {
			// Arrange
			BindingResult bindingResult = new BeanPropertyBindingResult(new PageQuery(), "pageQuery");
			bindingResult.addError(new FieldError("pageQuery", "keyword", "关键词过长"));
			org.springframework.validation.BindException exception = new org.springframework.validation.BindException(
					bindingResult);

			// Act
			Result<Void> result = handler.handleBind(exception);

			// Assert
			assertThat(result.getCode()).isEqualTo("999901");
			assertThat(result.getMsg()).isEqualTo("keyword: 关键词过长");
		}

	}

	@Nested
	@DisplayName("未知异常处理")
	class HandleException {

		@Test
		@DisplayName("任意运行时异常 - 返回 999999 系统异常且不泄漏原始消息")
		void handleException_runtimeException_returnsSystemError() {
			// Arrange
			Exception exception = new RuntimeException("内部堆栈信息不应外泄");

			// Act
			Result<Void> result = handler.handleException(exception);

			// Assert
			assertThat(result.getCode()).isEqualTo(ErrorCode.SYSTEM_ERROR.getCode());
			assertThat(result.getMsg()).isEqualTo("系统异常");
			assertThat(result.getData()).isNull();
		}

	}

}
