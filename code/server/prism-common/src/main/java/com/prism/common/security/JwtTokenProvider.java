package com.prism.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * JWT Token 生成与解析（OAuth2 Resource Server + RS256）
 */
@Slf4j
@Component
public class JwtTokenProvider {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${jwt.access-expire-hours:2}")
    private int accessExpireHours;

    @Value("${jwt.refresh-expire-days:7}")
    private int refreshExpireDays;

    @Value("${jwt.remember-refresh-expire-days:30}")
    private int rememberRefreshExpireDays;

    public JwtTokenProvider(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    /**
     * 生成 Access Token
     */
    public String generateAccessToken(Long userId, String username) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(String.valueOf(userId))
                .claim("username", username)
                .claim("type", "access")
                .issuedAt(now)
                .expiresAt(now.plusSeconds((long) accessExpireHours * 3600))
                .build();

        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    /**
     * 生成 Refresh Token
     */
    public String generateRefreshToken(Long userId, String username, boolean rememberMe) {
        int days = rememberMe ? rememberRefreshExpireDays : refreshExpireDays;
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(String.valueOf(userId))
                .claim("username", username)
                .claim("type", "refresh")
                .issuedAt(now)
                .expiresAt(now.plusSeconds((long) days * 86400))
                .build();

        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    /**
     * 解析 Token，返回 Jwt 对象
     */
    public Jwt parseToken(String token) {
        return jwtDecoder.decode(token);
    }

    /**
     * 验证 Token 是否有效
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("JWT Token 无效: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 从 Token 中获取 userId
     */
    public Long getUserId(String token) {
        Jwt jwt = parseToken(token);
        return Long.valueOf(jwt.getSubject());
    }

    /**
     * 从 Token 中获取 username
     */
    public String getUsername(String token) {
        Jwt jwt = parseToken(token);
        return jwt.getClaimAsString("username");
    }

    /**
     * 获取 Access Token 过期秒数
     */
    public int getAccessExpireSeconds() {
        return accessExpireHours * 3600;
    }

    /**
     * 获取 Refresh Token 过期秒数
     */
    public int getRefreshExpireSeconds(boolean rememberMe) {
        int days = rememberMe ? rememberRefreshExpireDays : refreshExpireDays;
        return days * 86400;
    }
}
