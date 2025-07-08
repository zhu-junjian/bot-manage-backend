package com.mirrormetech.corm.common.security;

import com.mirrormetech.corm.core.user.infra.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    public Long id;

    public String username;

    public String nickName;

    public String avatarUrl;

    public String email;

    public String password;

    public User currentUser;

    public Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String nickName, String avatarUrl, String password, String email,
                           Collection<? extends GrantedAuthority> authorities, User user) {
        this.id = id;
        this.username = username;
        this.nickName = nickName;
        this.avatarUrl = avatarUrl;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
        this.currentUser = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public User getUser(){
        return this.currentUser;
    }
}
