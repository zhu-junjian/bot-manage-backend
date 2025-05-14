package com.mirrormetech.corm.core.user.service;

import com.mirrormetech.corm.common.security.UserDetailsImpl;
import com.mirrormetech.corm.core.user.domain.RoleRepository;
import com.mirrormetech.corm.core.user.domain.UserRepository;
import com.mirrormetech.corm.core.user.infra.Role;
import com.mirrormetech.corm.core.user.infra.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 从数据库查询用户
        User user = userRepository.findByUsername(username);
        List<Role> roles = new ArrayList<>();
        roleRepository.findRolesByUserId(user.getId()).forEach(roles::add);
        user.setRoles(roles);
        // 2. 转换用户角色为 GrantedAuthority（权限标识）
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().toLowerCase()))
                .collect(Collectors.toList());

        // 3. 返回 UserDetails 对象
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getNickName(),
                user.getAvatarUrl(),
                user.getPassword(),
                user.getEmail(),
                authorities
        );
    }
}