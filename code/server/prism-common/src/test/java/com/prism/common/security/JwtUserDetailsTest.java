package com.prism.common.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JWT 用户详情 JwtUserDetails 测试
 */
@DisplayName("JWT 用户详情 JwtUserDetails")
class JwtUserDetailsTest {

	@Nested
	@DisplayName("构建与取值")
	class BuildAndGet {

		@Test
		@DisplayName("Builder 构建 - 各字段正确赋值")
		void builder_allFields_valuesPreserved() {
			// Arrange
			List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"),
					new SimpleGrantedAuthority("system:user:list"));

			// Act
			JwtUserDetails details = JwtUserDetails.builder()
				.userId(10L)
				.username("zhangsan")
				.password("secret")
				.authorities(authorities)
				.build();

			// Assert
			assertThat(details.getUserId()).isEqualTo(10L);
			assertThat(details.getUsername()).isEqualTo("zhangsan");
			assertThat(details.getPassword()).isEqualTo("secret");
			assertThat(details.getAuthorities()).isEqualTo(authorities);
		}

		@Test
		@DisplayName("全参构造 - 各字段正确赋值")
		void allArgsConstructor_allFields_valuesPreserved() {
			// Arrange
			List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

			// Act
			JwtUserDetails details = new JwtUserDetails(3L, "lisi", "pwd", authorities);

			// Assert
			assertThat(details.getUserId()).isEqualTo(3L);
			assertThat(details.getUsername()).isEqualTo("lisi");
			assertThat(details.getPassword()).isEqualTo("pwd");
			assertThat(details.getAuthorities()).hasSize(1);
		}

		@Test
		@DisplayName("无参构造 + Setter - 字段可更新")
		void noArgsConstructor_thenSetters_fieldsUpdated() {
			// Arrange
			JwtUserDetails details = new JwtUserDetails();

			// Act
			details.setUserId(5L);
			details.setUsername("wangwu");

			// Assert
			assertThat(details.getUserId()).isEqualTo(5L);
			assertThat(details.getUsername()).isEqualTo("wangwu");
		}

	}

	@Nested
	@DisplayName("UserDetails 状态方法")
	class UserDetailsState {

		@Test
		@DisplayName("任意实例 - 账户状态方法均返回 true")
		void stateMethods_anyInstance_allReturnTrue() {
			// Arrange
			JwtUserDetails details = JwtUserDetails.builder().userId(1L).username("u").build();

			// Act & Assert
			assertThat(details.isAccountNonExpired()).isTrue();
			assertThat(details.isAccountNonLocked()).isTrue();
			assertThat(details.isCredentialsNonExpired()).isTrue();
			assertThat(details.isEnabled()).isTrue();
		}

	}

	@Test
	@DisplayName("equals/hashCode - 相同字段实例相等")
	void equals_sameFields_instancesEqual() {
		// Arrange
		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
		JwtUserDetails a = JwtUserDetails.builder()
			.userId(1L)
			.username("u")
			.password("p")
			.authorities(authorities)
			.build();
		JwtUserDetails b = JwtUserDetails.builder()
			.userId(1L)
			.username("u")
			.password("p")
			.authorities(authorities)
			.build();

		// Act & Assert
		assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
		assertThat(a.toString()).contains("u");
	}

}
