package com.prism.common.security;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

import java.lang.reflect.Field;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * JWT Token 生成与解析 JwtTokenProvider 测试（内存 RSA 密钥对，真实编码/解码）
 */
@DisplayName("JWT Token 提供者 JwtTokenProvider")
class JwtTokenProviderTest {

	private static RSAPublicKey publicKey;

	private static RSAPrivateKey privateKey;

	private static NimbusJwtEncoder encoder;

	private JwtTokenProvider provider;

	@BeforeAll
	static void generateKeyPair() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		KeyPair keyPair = generator.generateKeyPair();
		publicKey = (RSAPublicKey) keyPair.getPublic();
		privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAKey rsaKey = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
		encoder = new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(rsaKey)));
	}

	@BeforeEach
	void setUp() throws Exception {
		provider = new JwtTokenProvider(encoder, NimbusJwtDecoder.withPublicKey(publicKey).build());
		setField(provider, "accessExpireHours", 2);
		setField(provider, "refreshExpireDays", 7);
		setField(provider, "rememberRefreshExpireDays", 30);
	}

	private static void setField(Object target, String name, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(name);
		field.setAccessible(true);
		field.set(target, value);
	}

	/** 直接使用 encoder 构造任意 claims 的 Token（用于过期/非法 subject 场景） */
	private static String encodeRaw(JwtClaimsSet claims) {
		return encoder.encode(JwtEncoderParameters.from(JwsHeader.with(SignatureAlgorithm.RS256).build(), claims))
			.getTokenValue();
	}

	@Nested
	@DisplayName("生成 Access Token")
	class GenerateAccessToken {

		@Test
		@DisplayName("合法用户 - claims 包含 subject/username/type 与 2 小时有效期")
		void generateAccessToken_validUser_claimsCorrect() {
			// Act
			String token = provider.generateAccessToken(100L, "zhangsan");

			// Assert
			Jwt jwt = provider.parseToken(token);
			assertThat(jwt.getSubject()).isEqualTo("100");
			assertThat(jwt.getClaimAsString("username")).isEqualTo("zhangsan");
			assertThat(jwt.getClaimAsString("type")).isEqualTo("access");
			assertThat(jwt.getIssuedAt()).isNotNull();
			assertThat(Duration.between(jwt.getIssuedAt(), jwt.getExpiresAt()).toHours()).isEqualTo(2);
		}

	}

	@Nested
	@DisplayName("生成 Refresh Token")
	class GenerateRefreshToken {

		@ParameterizedTest(name = "rememberMe={0} → 有效期 {1} 天")
		@CsvSource({ "false, 7", "true, 30" })
		@DisplayName("记住我开关 - 有效期分别为 7/30 天且 type=refresh")
		void generateRefreshToken_rememberMeFlag_expireDaysDiffer(boolean rememberMe, long expectedDays) {
			// Act
			String token = provider.generateRefreshToken(200L, "lisi", rememberMe);

			// Assert
			Jwt jwt = provider.parseToken(token);
			assertThat(jwt.getSubject()).isEqualTo("200");
			assertThat(jwt.getClaimAsString("type")).isEqualTo("refresh");
			assertThat(Duration.between(jwt.getIssuedAt(), jwt.getExpiresAt()).toDays()).isEqualTo(expectedDays);
		}

	}

	@Nested
	@DisplayName("解析与校验 Token")
	class ParseAndValidate {

		@Test
		@DisplayName("合法 Token - parseToken 返回 Jwt 对象")
		void parseToken_validToken_returnsJwt() {
			// Arrange
			String token = provider.generateAccessToken(1L, "admin");

			// Act
			Jwt jwt = provider.parseToken(token);

			// Assert
			assertThat(jwt).isNotNull();
			assertThat(jwt.getTokenValue()).isEqualTo(token);
		}

		@Test
		@DisplayName("合法 Token - validateToken 返回 true")
		void validateToken_validToken_returnsTrue() {
			// Arrange
			String token = provider.generateAccessToken(1L, "admin");

			// Act & Assert
			assertThat(provider.validateToken(token)).isTrue();
		}

		@Test
		@DisplayName("被篡改的 Token - validateToken 返回 false")
		void validateToken_tamperedToken_returnsFalse() {
			// Arrange
			String token = provider.generateAccessToken(1L, "admin");
			int mid = token.length() / 2;
			char original = token.charAt(mid);
			char replaced = (original == 'a') ? 'b' : 'a';
			String tampered = token.substring(0, mid) + replaced + token.substring(mid + 1);

			// Act & Assert
			assertThat(provider.validateToken(tampered)).isFalse();
		}

		@Test
		@DisplayName("已过期的 Token - validateToken 返回 false")
		void validateToken_expiredToken_returnsFalse() {
			// Arrange
			Instant past = Instant.now().minusSeconds(7200);
			String expired = encodeRaw(JwtClaimsSet.builder()
				.subject("1")
				.claim("username", "admin")
				.issuedAt(past.minusSeconds(3600))
				.expiresAt(past)
				.build());

			// Act & Assert
			assertThat(provider.validateToken(expired)).isFalse();
		}

		@Test
		@DisplayName("非 JWT 字符串 - validateToken 返回 false")
		void validateToken_garbageString_returnsFalse() {
			// Act & Assert
			assertThat(provider.validateToken("not-a-jwt-token")).isFalse();
		}

		@Test
		@DisplayName("已过期的 Token - parseToken 抛出 JwtException")
		void parseToken_expiredToken_throwsJwtException() {
			// Arrange
			Instant past = Instant.now().minusSeconds(60);
			String expired = encodeRaw(JwtClaimsSet.builder()
				.subject("1")
				.issuedAt(past.minusSeconds(60))
				.expiresAt(past)
				.build());

			// Act & Assert
			assertThatThrownBy(() -> provider.parseToken(expired)).isInstanceOf(JwtException.class);
		}

	}

	@Nested
	@DisplayName("从 Token 提取用户信息")
	class ExtractUserInfo {

		@Test
		@DisplayName("合法 Token - 提取 userId 与 username")
		void getUserIdAndGetUsername_validToken_returnsValues() {
			// Arrange
			String token = provider.generateAccessToken(9527L, "zhouxingchi");

			// Act & Assert
			assertThat(provider.getUserId(token)).isEqualTo(9527L);
			assertThat(provider.getUsername(token)).isEqualTo("zhouxingchi");
		}

		@Test
		@DisplayName("subject 非数字 - getUserId 抛出 NumberFormatException")
		void getUserId_nonNumericSubject_throwsNumberFormatException() {
			// Arrange
			Instant now = Instant.now();
			String token = encodeRaw(JwtClaimsSet.builder()
				.subject("abc")
				.issuedAt(now)
				.expiresAt(now.plusSeconds(3600))
				.build());

			// Act & Assert
			assertThatThrownBy(() -> provider.getUserId(token)).isInstanceOf(NumberFormatException.class);
		}

	}

	@Nested
	@DisplayName("过期秒数计算")
	class ExpireSeconds {

		@Test
		@DisplayName("默认配置 - Access 过期秒数为 2 小时")
		void getAccessExpireSeconds_default_returns7200() {
			// Act & Assert
			assertThat(provider.getAccessExpireSeconds()).isEqualTo(7200);
		}

		@ParameterizedTest(name = "rememberMe={0} → {1} 秒")
		@CsvSource({ "false, 604800", "true, 2592000" })
		@DisplayName("记住我开关 - Refresh 过期秒数分别为 7/30 天")
		void getRefreshExpireSeconds_rememberMeFlag_returnsExpected(boolean rememberMe, int expected) {
			// Act & Assert
			assertThat(provider.getRefreshExpireSeconds(rememberMe)).isEqualTo(expected);
		}

		@Test
		@DisplayName("自定义过期配置 - 秒数按新配置计算")
		void getExpireSeconds_customConfig_recomputed() throws Exception {
			// Arrange
			setField(provider, "accessExpireHours", 1);
			setField(provider, "refreshExpireDays", 1);
			setField(provider, "rememberRefreshExpireDays", 2);

			// Act & Assert
			assertThat(provider.getAccessExpireSeconds()).isEqualTo(3600);
			assertThat(provider.getRefreshExpireSeconds(false)).isEqualTo(86400);
			assertThat(provider.getRefreshExpireSeconds(true)).isEqualTo(172800);
		}

	}

}
