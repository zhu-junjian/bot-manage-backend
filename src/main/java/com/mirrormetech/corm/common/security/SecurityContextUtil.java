package com.mirrormetech.corm.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 认证上下文
 * @author spencer
 * @date 2025/04/29
 */
@Component
public class SecurityContextUtil {

    public UserDetailsImpl getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        Long currentUserId = principal.id;
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     *
     * @return 当前登录用户Id
     */
    public Long getCurrentUserId() {
        return getCurrentUser().id;
    }
}
