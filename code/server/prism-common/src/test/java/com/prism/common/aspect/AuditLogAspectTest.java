package com.prism.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prism.common.context.RequestContextHolder;
import com.prism.common.security.JwtUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 审计日志切面 AuditLogAspect 测试
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("审计日志切面 AuditLogAspect")
class AuditLogAspectTest {

	@Mock
	private AuditLogService auditLogService;

	@Mock
	private ProceedingJoinPoint joinPoint;

	private AuditLogAspect aspect;

	/** 通过测试用目标方法获取真实的 @AuditLog 注解实例 */
	private AuditLog auditLogAnnotation;

	/** 通过测试用目标方法获取真实的 @LoginLog 注解实例 */
	private LoginLog loginLogAnnotation;

	static class SampleService {

		@AuditLog(module = "用户管理", operation = "新增用户", type = "CREATE")
		public String create(String name) {
			return name;
		}

		@LoginLog("用户登录")
		public Object login(Object dto) {
			return dto;
		}

	}

	/** 模拟登录入参（含账号标识） */
	static class FakeLoginDTO {

		public String getUsername() {
			return "zhangsan";
		}

		public String getPassword() {
			return "secret";
		}

	}

	/** 模拟登录返回结果的用户信息 */
	static class FakeUserInfo {

		public Long getId() {
			return 9L;
		}

		public String getUsername() {
			return "zhangsan";
		}

	}

	/** 模拟登录返回结果 LoginVO */
	static class FakeLoginVO {

		public FakeUserInfo getUserInfo() {
			return new FakeUserInfo();
		}

	}

	@BeforeEach
	void setUp() throws Exception {
		aspect = new AuditLogAspect(auditLogService, new ObjectMapper());
		Method method = SampleService.class.getMethod("create", String.class);
		auditLogAnnotation = method.getAnnotation(AuditLog.class);
		Method loginMethod = SampleService.class.getMethod("login", Object.class);
		loginLogAnnotation = loginMethod.getAnnotation(LoginLog.class);
	}

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
		org.springframework.web.context.request.RequestContextHolder.resetRequestAttributes();
		RequestContextHolder.clear();
	}

	private void login(Long userId, String username) {
		JwtUserDetails userDetails = JwtUserDetails.builder().userId(userId).username(username).build();
		SecurityContextHolder.getContext()
			.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, List.of()));
	}

	private HttpServletRequest mockRequest() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		org.springframework.web.context.request.RequestContextHolder
			.setRequestAttributes(new ServletRequestAttributes(request));
		return request;
	}

	@Nested
	@DisplayName("环绕通知 - 业务执行结果")
	class Around {

		@Test
		@DisplayName("业务方法成功 - 返回结果且记录 SUCCESS 日志")
		void around_methodSucceeds_returnsResultAndLogsSuccess() throws Throwable {
			// Arrange
			login(9L, "zhangsan");
			RequestContextHolder.setRequestId("rid-1");
			when(joinPoint.proceed()).thenReturn("done");
			when(joinPoint.getArgs()).thenReturn(new Object[] { "arg1" });

			// Act
			Object result = aspect.around(joinPoint, auditLogAnnotation);

			// Assert
			assertThat(result).isEqualTo("done");
			ArgumentCaptor<String> resultCaptor = ArgumentCaptor.forClass(String.class);
			verify(auditLogService).saveAuditLog(any(), anyString(), anyString(), anyString(), anyString(),
					resultCaptor.capture(), anyString(), any(), any(), anyString());
			assertThat(resultCaptor.getValue()).isEqualTo("SUCCESS");
		}

		@Test
		@DisplayName("业务方法成功 - 日志携带注解元数据/用户/请求ID/参数JSON")
		void around_methodSucceeds_logsMetadata() throws Throwable {
			// Arrange
			login(9L, "zhangsan");
			RequestContextHolder.setRequestId("rid-2");
			HttpServletRequest request = mockRequest();
			lenient().when(request.getRemoteAddr()).thenReturn("10.0.0.1");
			lenient().when(request.getHeader("User-Agent")).thenReturn("JUnit");
			when(joinPoint.proceed()).thenReturn("ok");
			when(joinPoint.getArgs()).thenReturn(new Object[] { "张三" });

			// Act
			aspect.around(joinPoint, auditLogAnnotation);

			// Assert
			verify(auditLogService).saveAuditLog(9L, "zhangsan", "用户管理", "新增用户", "CREATE", "SUCCESS", "10.0.0.1",
					"rid-2", "JUnit", "[\"张三\"]");
		}

		@Test
		@DisplayName("业务方法抛异常 - 原样抛出且记录 FAIL 日志")
		void around_methodThrows_rethrowsAndLogsFail() throws Throwable {
			// Arrange
			when(joinPoint.proceed()).thenThrow(new IllegalStateException("boom"));
			when(joinPoint.getArgs()).thenReturn(new Object[0]);

			// Act & Assert
			assertThatThrownBy(() -> aspect.around(joinPoint, auditLogAnnotation))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("boom");
			verify(auditLogService).saveAuditLog(any(), any(), anyString(), anyString(), anyString(),
					org.mockito.ArgumentMatchers.eq("FAIL"), anyString(), any(), any(), anyString());
		}

		@Test
		@DisplayName("日志组件抛异常 - 被 finally 捕获且不影响业务结果")
		void around_logSavingThrows_businessResultUnaffected() throws Throwable {
			// Arrange
			when(joinPoint.proceed()).thenReturn("ok");
			when(joinPoint.getArgs()).thenReturn(new Object[0]);
			doThrow(new RuntimeException("db down")).when(auditLogService)
				.saveAuditLog(any(), any(), anyString(), anyString(), anyString(), anyString(), anyString(), any(),
						any(), anyString());

			// Act
			Object result = aspect.around(joinPoint, auditLogAnnotation);

			// Assert
			assertThat(result).isEqualTo("ok");
		}

	}

	@Nested
	@DisplayName("客户端 IP 解析")
	class ClientIp {

		@ParameterizedTest(name = "XFF=[{0}] Proxy=[{1}] WL=[{2}] Remote={3} → IP={4}")
		@CsvSource(delimiter = '|', value = { "1.2.3.4, 5.6.7.8|null|null|null|1.2.3.4",
				"unknown|9.9.9.9|null|null|9.9.9.9", "unknown|unknown|8.8.8.8|null|8.8.8.8",
				"null|null|null|192.168.1.1|192.168.1.1" })
		@DisplayName("按 XFF→Proxy→WL→RemoteAddr 顺序取 IP 且多值取第一个")
		void around_variousHeaders_ipResolvedByPriority(String xff, String proxy, String wl, String remote,
				String expectedIp) throws Throwable {
			// Arrange
			HttpServletRequest request = mockRequest();
			lenient().when(request.getHeader("X-Forwarded-For")).thenReturn("null".equals(xff) ? null : xff);
			lenient().when(request.getHeader("Proxy-Client-IP")).thenReturn("null".equals(proxy) ? null : proxy);
			lenient().when(request.getHeader("WL-Proxy-Client-IP")).thenReturn("null".equals(wl) ? null : wl);
			lenient().when(request.getRemoteAddr()).thenReturn("null".equals(remote) ? null : remote);
			when(joinPoint.proceed()).thenReturn("ok");
			when(joinPoint.getArgs()).thenReturn(new Object[0]);

			// Act
			aspect.around(joinPoint, auditLogAnnotation);

			// Assert
			ArgumentCaptor<String> ipCaptor = ArgumentCaptor.forClass(String.class);
			verify(auditLogService).saveAuditLog(any(), any(), anyString(), anyString(), anyString(), anyString(),
					ipCaptor.capture(), any(), any(), anyString());
			assertThat(ipCaptor.getValue()).isEqualTo(expectedIp);
		}

		@Test
		@DisplayName("无请求上下文 - IP 与 UserAgent 为空串且流程不中断")
		void around_noRequestAttributes_ipAndUserAgentEmpty() throws Throwable {
			// Arrange
			when(joinPoint.proceed()).thenReturn("ok");
			when(joinPoint.getArgs()).thenReturn(new Object[0]);

			// Act
			aspect.around(joinPoint, auditLogAnnotation);

			// Assert
			verify(auditLogService).saveAuditLog(any(), any(), anyString(), anyString(), anyString(), anyString(),
					org.mockito.ArgumentMatchers.eq(""), any(), org.mockito.ArgumentMatchers.eq(""), anyString());
		}

	}

	@Nested
	@DisplayName("参数序列化")
	class SerializeArgs {

		@Test
		@DisplayName("ObjectMapper 抛异常 - detail 降级为空串")
		void around_serializationFails_detailEmpty() throws Throwable {
			// Arrange
			ObjectMapper failingMapper = mock(ObjectMapper.class);
			when(failingMapper.writeValueAsString(any())).thenThrow(new RuntimeException("serialize error"));
			AuditLogAspect failingAspect = new AuditLogAspect(auditLogService, failingMapper);
			when(joinPoint.proceed()).thenReturn("ok");
			when(joinPoint.getArgs()).thenReturn(new Object[] { new Object() });

			// Act
			failingAspect.around(joinPoint, auditLogAnnotation);

			// Assert
			verify(auditLogService).saveAuditLog(any(), any(), anyString(), anyString(), anyString(), anyString(),
					anyString(), any(), any(), org.mockito.ArgumentMatchers.eq(""));
		}

	}

	@Nested
	@DisplayName("登录日志环绕通知")
	class LoginLogAdvice {

		@Test
		@DisplayName("登录成功（无安全上下文）- 从返回结果提取用户并记录 SUCCESS")
		void aroundLogin_success_extractsUserFromResult() throws Throwable {
			// Arrange
			RequestContextHolder.setRequestId("rid-login-1");
			when(joinPoint.proceed()).thenReturn(new FakeLoginVO());
			when(joinPoint.getArgs()).thenReturn(new Object[] { new FakeLoginDTO() });

			// Act
			Object result = aspect.aroundLogin(joinPoint, loginLogAnnotation);

			// Assert
			assertThat(result).isInstanceOf(FakeLoginVO.class);
			verify(auditLogService).saveLoginLog(9L, "zhangsan", "", "", "SUCCESS", null, "rid-login-1");
		}

		@Test
		@DisplayName("登录失败 - 原样抛出且记录 FAIL/失败原因/入参用户名")
		void aroundLogin_failure_logsFailWithReasonAndArgUsername() throws Throwable {
			// Arrange
			RequestContextHolder.setRequestId("rid-login-2");
			when(joinPoint.proceed())
				.thenThrow(new com.prism.common.exception.BusinessException("999904", "用户名或密码错误"));
			when(joinPoint.getArgs()).thenReturn(new Object[] { new FakeLoginDTO() });

			// Act & Assert
			assertThatThrownBy(() -> aspect.aroundLogin(joinPoint, loginLogAnnotation))
				.isInstanceOf(com.prism.common.exception.BusinessException.class)
				.hasMessage("用户名或密码错误");
			verify(auditLogService).saveLoginLog(null, "zhangsan", "", "", "FAIL", "用户名或密码错误",
					"rid-login-2");
		}

		@Test
		@DisplayName("登出（已有安全上下文）- 用户信息取自上下文")
		void aroundLogin_logout_usesSecurityContext() throws Throwable {
			// Arrange
			login(9L, "zhangsan");
			RequestContextHolder.setRequestId("rid-login-3");
			when(joinPoint.proceed()).thenReturn(null);
			when(joinPoint.getArgs()).thenReturn(new Object[] { "token" });

			// Act
			aspect.aroundLogin(joinPoint, loginLogAnnotation);

			// Assert
			verify(auditLogService).saveLoginLog(9L, "zhangsan", "", "", "SUCCESS", null, "rid-login-3");
		}

		@Test
		@DisplayName("日志组件抛异常 - 被 finally 捕获且不影响业务结果")
		void aroundLogin_logSavingThrows_businessResultUnaffected() throws Throwable {
			// Arrange
			when(joinPoint.proceed()).thenReturn(new FakeLoginVO());
			when(joinPoint.getArgs()).thenReturn(new Object[0]);
			doThrow(new RuntimeException("db down")).when(auditLogService)
				.saveLoginLog(any(), anyString(), anyString(), anyString(), anyString(), any(), anyString());

			// Act
			Object result = aspect.aroundLogin(joinPoint, loginLogAnnotation);

			// Assert
			assertThat(result).isInstanceOf(FakeLoginVO.class);
		}

	}

	@Nested
	@DisplayName("异步保存入口")
	class AsyncSave {

		@Test
		@DisplayName("异步保存审计日志 - 透传全部参数")
		void asyncSaveAuditLog_normal_delegatesToService() {
			// Act
			aspect.asyncSaveAuditLog(1L, "admin", "角色管理", "删除角色", "DELETE", "SUCCESS", "127.0.0.1", "rid",
					"UA", "[]");

			// Assert
			verify(auditLogService).saveAuditLog(1L, "admin", "角色管理", "删除角色", "DELETE", "SUCCESS", "127.0.0.1",
					"rid", "UA", "[]");
		}

		@Test
		@DisplayName("异步保存审计日志失败 - 异常被吞掉不上抛")
		void asyncSaveAuditLog_serviceThrows_exceptionSwallowed() {
			// Arrange
			doThrow(new RuntimeException("db down")).when(auditLogService)
				.saveAuditLog(any(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
						anyString(), anyString(), anyString());

			// Act & Assert：不抛异常即为通过
			aspect.asyncSaveAuditLog(1L, "admin", "m", "o", "t", "SUCCESS", "ip", "rid", "ua", "detail");
			verify(auditLogService).saveAuditLog(any(), anyString(), anyString(), anyString(), anyString(), anyString(),
					anyString(), anyString(), anyString(), anyString());
		}

		@Test
		@DisplayName("异步保存登录日志 - 透传全部参数")
		void asyncSaveLoginLog_normal_delegatesToService() {
			// Act
			aspect.asyncSaveLoginLog(2L, "lisi", "10.1.1.1", "Chrome", "SUCCESS", null, "rid-9");

			// Assert
			verify(auditLogService).saveLoginLog(2L, "lisi", "10.1.1.1", "Chrome", "SUCCESS", null, "rid-9");
		}

		@Test
		@DisplayName("异步保存登录日志失败 - 异常被吞掉不上抛")
		void asyncSaveLoginLog_serviceThrows_exceptionSwallowed() {
			// Arrange
			doThrow(new RuntimeException("db down")).when(auditLogService)
				.saveLoginLog(any(), anyString(), anyString(), anyString(), anyString(), any(), anyString());

			// Act & Assert：不抛异常即为通过
			aspect.asyncSaveLoginLog(2L, "lisi", "ip", "ua", "FAIL", "密码错误", "rid");
			verify(auditLogService, never()).saveAuditLog(any(), any(), any(), any(), any(), any(), any(), any(), any(),
					any());
		}

	}

}
