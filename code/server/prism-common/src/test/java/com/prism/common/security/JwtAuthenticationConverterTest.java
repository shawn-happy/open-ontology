package com.prism.common.security;

import com.prism.common.context.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * JWT → Authentication 转换器测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("JWT 认证转换器 JwtAuthenticationConverter")
class JwtAuthenticationConverterTest {

	private static final String PERMS_KEY = "prism:user:perms:100";

	@Mock
	private StringRedisTemplate stringRedisTemplate;

	@Mock
	private ValueOperations<String, String> valueOperations;

	private JwtAuthenticationConverter converter;

	@BeforeEach
	void setUp() {
		converter = new JwtAuthenticationConverter(stringRedisTemplate);
	}

	@AfterEach
	void tearDown() {
		RequestContextHolder.clear();
	}

	private Jwt buildJwt() {
		Instant now = Instant.now();
		return new Jwt("token-value", now, now.plusSeconds(3600), Map.of("alg", "RS256"),
				Map.of("sub", "100", "username", "zhangsan"));
	}

	@Nested
	@DisplayName("转换成功场景")
	class Convert {

		@Test
		@DisplayName("Redis 有细粒度权限 - principal 为 JwtUserDetails 且权限含 ROLE_USER 与缓存权限")
		void convert_withCachedPerms_authoritiesMerged() {
			// Arrange
			when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
			when(valueOperations.get(PERMS_KEY)).thenReturn("system:user:list,system:role:list");

			// Act
			AbstractAuthenticationToken token = converter.convert(buildJwt());

			// Assert
			assertThat(token).isNotNull();
			assertThat(token.isAuthenticated()).isTrue();
			assertThat(token.getPrincipal()).isInstanceOf(JwtUserDetails.class);
			JwtUserDetails userDetails = (JwtUserDetails) token.getPrincipal();
			assertThat(userDetails.getUserId()).isEqualTo(100L);
			assertThat(userDetails.getUsername()).isEqualTo("zhangsan");
			assertThat(userDetails.getAuthorities()).extracting(GrantedAuthority::getAuthority)
				.containsExactly("ROLE_USER", "system:user:list", "system:role:list");
		}

		@Test
		@DisplayName("转换后 - userId 写入 RequestContextHolder")
		void convert_anyToken_setsRequestContextUserId() {
			// Arrange
			when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
			when(valueOperations.get(PERMS_KEY)).thenReturn(null);

			// Act
			converter.convert(buildJwt());

			// Assert
			assertThat(RequestContextHolder.getUserId()).isEqualTo(100L);
		}

		@Test
		@DisplayName("权限串含空白项 - 空白项被过滤且其余权限 trim 后保留")
		void convert_permsWithBlanks_blanksFiltered() {
			// Arrange
			when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
			when(valueOperations.get(PERMS_KEY)).thenReturn(" system:user:list , ,system:role:list,");

			// Act
			AbstractAuthenticationToken token = converter.convert(buildJwt());

			// Assert
			JwtUserDetails userDetails = (JwtUserDetails) token.getPrincipal();
			assertThat(userDetails.getAuthorities()).extracting(GrantedAuthority::getAuthority)
				.containsExactly("ROLE_USER", "system:user:list", "system:role:list");
		}

	}

	@Nested
	@DisplayName("Redis 无权限缓存场景")
	class NoCachedPerms {

		@ParameterizedTest(name = "缓存值为 [{0}]")
		@NullAndEmptySource
		@ValueSource(strings = { "   ", "\t" })
		@DisplayName("缓存为 null/空白 - 仅保留 ROLE_USER")
		void convert_blankPerms_onlyRoleUser(String perms) {
			// Arrange
			when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
			when(valueOperations.get(PERMS_KEY)).thenReturn(perms);

			// Act
			AbstractAuthenticationToken token = converter.convert(buildJwt());

			// Assert
			assertThat(token.getAuthorities()).extracting(GrantedAuthority::getAuthority).containsExactly("ROLE_USER");
		}

	}

}
