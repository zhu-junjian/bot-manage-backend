package com.mirrormetech.corm.common.security;

import com.mirrormetech.corm.core.user.infra.User;
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

    public UserDetailsImpl getCurrentUserDetail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        Long currentUserId = principal.id;
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 查询当前登录用户的userId
     * @return 当前登录用户Id
     */
    public Long getCurrentUserId() {
        return getCurrentUserDetail().id;
    }

    /**
     * 获取当前登录的用户所有信息
     * @return 用户实体
     */
    public User getCurrentUser() {
        return getCurrentUserDetail().getUser();}
}
