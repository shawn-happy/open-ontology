package com.prism.common.aspect;

import com.prism.common.security.JwtUserDetails;
import com.prism.common.security.annotation.RequirePermission;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 权限校验切面 PermissionAspect 测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("权限校验切面 PermissionAspect")
class PermissionAspectTest {

	private final PermissionAspect aspect = new PermissionAspect();

	@Mock
	private ProceedingJoinPoint joinPoint;

	/** 携带真实注解的目标方法，供反射获取 @RequirePermission 实例 */
	static class SampleController {

		@RequirePermission("system:user:create")
		public void create() {
		}

	}

	private RequirePermission requirePermission;

	@BeforeEach
	void setUp() throws Exception {
		Method method = SampleController.class.getMethod("create");
		requirePermission = method.getAnnotation(RequirePermission.class);
	}

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
	}

	private void login(Long userId, String... permissions) {
		JwtUserDetails userDetails = JwtUserDetails.builder().userId(userId).username("u" + userId).build();
		List<SimpleGrantedAuthority> authorities = java.util.Arrays.stream(permissions)
			.map(SimpleGrantedAuthority::new)
			.toList();
		SecurityContextHolder.getContext()
			.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, authorities));
	}

	@Nested
	@DisplayName("放行场景")
	class Allowed {

		@Test
		@DisplayName("admin 用户（userId=1） - 无权限也直接放行")
		void checkPermission_adminUser_proceedsWithoutPermission() throws Throwable {
			// Arrange
			login(1L);
			when(joinPoint.proceed()).thenReturn("admin-ok");

			// Act
			Object result = aspect.checkPermission(joinPoint, requirePermission);

			// Assert
			assertThat(result).isEqualTo("admin-ok");
			verify(joinPoint).proceed();
		}

		@Test
		@DisplayName("普通用户持有目标权限 - 放行")
		void checkPermission_userWithPermission_proceeds() throws Throwable {
			// Arrange
			login(2L, "system:user:create");
			when(joinPoint.proceed()).thenReturn("ok");

			// Act
			Object result = aspect.checkPermission(joinPoint, requirePermission);

			// Assert
			assertThat(result).isEqualTo("ok");
			verify(joinPoint).proceed();
		}

	}

	@Nested
	@DisplayName("拒绝场景")
	class Denied {

		@Test
		@DisplayName("普通用户缺少目标权限 - 抛出 AccessDeniedException 且不执行业务")
		void checkPermission_userWithoutPermission_throwsAccessDenied() throws Throwable {
			// Arrange
			login(3L, "system:user:list");

			// Act & Assert
			assertThatThrownBy(() -> aspect.checkPermission(joinPoint, requirePermission))
				.isInstanceOf(AccessDeniedException.class)
				.hasMessageContaining("无权限访问：system:user:create");
			verify(joinPoint, never()).proceed();
		}

		@Test
		@DisplayName("未登录（无认证信息） - 抛出 AccessDeniedException")
		void checkPermission_noAuthentication_throwsAccessDenied() {
			// Act & Assert
			assertThatThrownBy(() -> aspect.checkPermission(joinPoint, requirePermission))
				.isInstanceOf(AccessDeniedException.class);
		}

		@Test
		@DisplayName("principal 非 JwtUserDetails（userId 为 null） - 抛出 AccessDeniedException")
		void checkPermission_nonJwtPrincipal_throwsAccessDenied() {
			// Arrange
			SecurityContextHolder.getContext()
				.setAuthentication(new UsernamePasswordAuthenticationToken("anonymous", null, List.of()));

			// Act & Assert
			assertThatThrownBy(() -> aspect.checkPermission(joinPoint, requirePermission))
				.isInstanceOf(AccessDeniedException.class);
		}

	}

}
