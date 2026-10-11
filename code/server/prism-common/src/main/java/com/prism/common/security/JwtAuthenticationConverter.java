package com.prism.common.security;

import com.prism.common.context.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * JWT → Authentication 转换器
 * 从 JWT claims 提取 userId/username，从 Redis 加载用户权限
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        String username = jwt.getClaimAsString("username");

        // 构建权限列表：ROLE_USER + Redis 缓存的细粒度权限
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        String permsKey = "prism:user:perms:" + userId;
        String permsStr = stringRedisTemplate.opsForValue().get(permsKey);
        if (permsStr != null && !permsStr.isBlank()) {
            for (String perm : permsStr.split(",")) {
                if (!perm.isBlank()) {
                    authorities.add(new SimpleGrantedAuthority(perm.trim()));
                }
            }
        }

        // 设置到 RequestContextHolder
        RequestContextHolder.setUserId(userId);

        JwtUserDetails userDetails = JwtUserDetails.builder()
                .userId(userId)
                .username(username)
                .authorities(authorities)
                .build();

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }
}
