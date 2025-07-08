package com.mirrormetech.corm.core.user.domain.vo;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginRequest {
    public String username;
    public String password;
    public String jwtToken;
    public String refreshToken;
}
