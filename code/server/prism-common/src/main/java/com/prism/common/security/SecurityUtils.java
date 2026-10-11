package com.prism.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * Security 工具类
 */
public final class SecurityUtils {

    private SecurityUtils() {}

    /**
     * 获取当前用户 ID
     */
    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return null;
        }
        if (auth.getPrincipal() instanceof JwtUserDetails userDetails) {
            return userDetails.getUserId();
        }
        return null;
    }

    /**
     * 获取当前用户名
     */
    public static String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return null;
        }
        if (auth.getPrincipal() instanceof JwtUserDetails userDetails) {
            return userDetails.getUsername();
        }
        return null;
    }

    /**
     * 检查当前用户是否拥有指定权限
     */
    public static boolean hasPermission(String permission) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities() == null) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        return authorities.stream()
                .anyMatch(a -> a.getAuthority().equals(permission));
    }
}
