package com.mirrormetech.corm.core.user;


import com.mirrormetech.corm.common.result.ApiResult;
import com.mirrormetech.corm.core.user.domain.vo.LoginRequest;
import com.mirrormetech.corm.core.user.domain.vo.LoginResponse;
import com.mirrormetech.corm.core.user.domain.vo.RegistrationRequest;
import com.mirrormetech.corm.core.user.infra.User;
import com.mirrormetech.corm.core.user.service.impl.LoginService;
import com.mirrormetech.corm.core.user.service.impl.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    // 登录应用服务
    private final LoginService loginService;

    // 注册应用服务
    private final RegistrationService registrationService;

     @PostMapping("/register")
    public ApiResult<LoginResponse> register(@RequestBody RegistrationRequest registrationRequest) {
         registrationService.registration(registrationRequest);
         return ApiResult.success();
    }

    @PostMapping("/login")
    public ApiResult<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse login = loginService.login(request);
        return ApiResult.success(new LoginResponse(login.getToken(), login.getMessage()));
    }

    @PostMapping
    public ApiResult<LoginResponse> query(@RequestBody LoginRequest request){
        return ApiResult.success();
    }

    //@PreAuthorize("@RolePermission.hasPermission('ADMIN')")
    @PostMapping("/queryUser")
    public ApiResult<List<User>> queryByUsername(@RequestBody LoginRequest request){

        return ApiResult.success();
    }
}
