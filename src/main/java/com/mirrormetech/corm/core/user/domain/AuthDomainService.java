package com.mirrormetech.corm.core.user.domain;

import com.mirrormetech.corm.core.user.infra.User;
import org.springframework.stereotype.Component;

/**
 * 认证领域服务
 */
@Component
public class AuthDomainService {

    /**
     * TODO 修改为从 securityContext -> authentication 中获取 用户信息
     * @param username
     * @param password
     * @return
     */
    public User authenticate(String username, String password) {
        return null;
    }
}
