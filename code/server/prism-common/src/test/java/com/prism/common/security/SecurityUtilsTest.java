package com.prism.common.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Security 工具类 SecurityUtils 测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Security 工具类 SecurityUtils")
class SecurityUtilsTest {

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
	}

	private void loginAs(JwtUserDetails userDetails, List<SimpleGrantedAuthority> authorities) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null,
				authorities);
		SecurityContextHolder.getContext().setAuthentication(token);
	}

	@Nested
	@DisplayName("获取当前用户 ID")
	class GetCurrentUserId {

		@Test
		@DisplayName("已登录 JwtUserDetails - 返回用户 ID")
		void getCurrentUserId_authenticated_returnsUserId() {
			// Arrange
			JwtUserDetails userDetails = JwtUserDetails.builder().userId(7L).username("zhangsan").build();
			loginAs(userDetails, List.of(new SimpleGrantedAuthority("ROLE_USER")));

			// Act & Assert
			assertThat(SecurityUtils.getCurrentUserId()).isEqualTo(7L);
		}

		@Test
		@DisplayName("上下文无认证信息 - 返回 null")
		void getCurrentUserId_noAuthentication_returnsNull() {
			// Act & Assert
			assertThat(SecurityUtils.getCurrentUserId()).isNull();
		}

		@Test
		@DisplayName("principal 为 null - 返回 null")
		void getCurrentUserId_nullPrincipal_returnsNull() {
			// Arrange
			Authentication auth = mock(Authentication.class);
			when(auth.getPrincipal()).thenReturn(null);
			SecurityContextHolder.getContext().setAuthentication(auth);

			// Act & Assert
			assertThat(SecurityUtils.getCurrentUserId()).isNull();
		}

		@Test
		@DisplayName("principal 非 JwtUserDetails（如匿名字符串） - 返回 null")
		void getCurrentUserId_nonJwtPrincipal_returnsNull() {
			// Arrange
			Authentication auth = new UsernamePasswordAuthenticationToken("anonymousUser", null, List.of());
			SecurityContextHolder.getContext().setAuthentication(auth);

			// Act & Assert
			assertThat(SecurityUtils.getCurrentUserId()).isNull();
		}

	}

	@Nested
	@DisplayName("获取当前用户名")
	class GetCurrentUsername {

		@Test
		@DisplayName("已登录 JwtUserDetails - 返回用户名")
		void getCurrentUsername_authenticated_returnsUsername() {
			// Arrange
			JwtUserDetails userDetails = JwtUserDetails.builder().userId(8L).username("lisi").build();
			loginAs(userDetails, List.of(new SimpleGrantedAuthority("ROLE_USER")));

			// Act & Assert
			assertThat(SecurityUtils.getCurrentUsername()).isEqualTo("lisi");
		}

		@Test
		@DisplayName("上下文无认证信息 - 返回 null")
		void getCurrentUsername_noAuthentication_returnsNull() {
			// Act & Assert
			assertThat(SecurityUtils.getCurrentUsername()).isNull();
		}

		@Test
		@DisplayName("principal 非 JwtUserDetails - 返回 null")
		void getCurrentUsername_nonJwtPrincipal_returnsNull() {
			// Arrange
			Authentication auth = new UsernamePasswordAuthenticationToken("somePrincipal", null, List.of());
			SecurityContextHolder.getContext().setAuthentication(auth);

			// Act & Assert
			assertThat(SecurityUtils.getCurrentUsername()).isNull();
		}

	}

	@Nested
	@DisplayName("权限判断")
	class HasPermission {

		@ParameterizedTest(name = "持有权限 [{0}] 查询 {1}")
		@CsvSource({ "system:user:list, system:user:list, true", "system:user:list, system:role:list, false",
				"system:user:list;system:role:list, system:role:list, true" })
		@DisplayName("按权限标识匹配 - 命中返回 true 否则 false")
		void hasPermission_authorityMatch_returnsExpected(String held, String queried, boolean expected) {
			// Arrange
			List<SimpleGrantedAuthority> authorities = java.util.Arrays.stream(held.split(";"))
				.map(SimpleGrantedAuthority::new)
				.toList();
			JwtUserDetails userDetails = JwtUserDetails.builder().userId(2L).username("wangwu").build();
			loginAs(userDetails, authorities);

			// Act & Assert
			assertThat(SecurityUtils.hasPermission(queried)).isEqualTo(expected);
		}

		@Test
		@DisplayName("上下文无认证信息 - 返回 false")
		void hasPermission_noAuthentication_returnsFalse() {
			// Act & Assert
			assertThat(SecurityUtils.hasPermission("any")).isFalse();
		}

		@Test
		@DisplayName("authorities 为 null - 返回 false")
		void hasPermission_nullAuthorities_returnsFalse() {
			// Arrange
			Authentication auth = mock(Authentication.class);
			when(auth.getAuthorities()).thenReturn(null);
			SecurityContextHolder.getContext().setAuthentication(auth);

			// Act & Assert
			assertThat(SecurityUtils.hasPermission("any")).isFalse();
		}

	}

}
