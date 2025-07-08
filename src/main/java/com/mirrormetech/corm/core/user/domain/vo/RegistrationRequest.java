package com.mirrormetech.corm.core.user.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 注册 命令的所有参数 在此类定义
 * 作为 统一对象 向外暴露粗粒度 具体类型 而非 用户名 密码
 * @author spencer
 * @date 2025/04/18
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class RegistrationRequest {

    private String username;
    private String password;
    private String email;
    private String phoneNum;
    private String identifyCode;

}
