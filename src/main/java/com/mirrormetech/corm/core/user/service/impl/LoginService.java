package com.mirrormetech.corm.core.user.service.impl;


import com.mirrormetech.corm.common.exception.ExceptionCode;
import com.mirrormetech.corm.common.exception.ServiceException;
import com.mirrormetech.corm.common.security.jwt.JwtProvider;
import com.mirrormetech.corm.core.user.domain.AuthDomainService;
import com.mirrormetech.corm.core.user.domain.repository.UserRepository;
import com.mirrormetech.corm.core.user.domain.vo.LoginRequest;
import com.mirrormetech.corm.core.user.domain.vo.LoginResponse;
import com.mirrormetech.corm.core.user.infra.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 认证审核
 * 应用服务（编排领域服务）
 */
@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthDomainService authDomainService;

    private final JwtProvider jwtProvider;

    private final JwtProvider jwtTokenProvider;

    @Qualifier("myBatisUserRepoImpl")
    private final UserRepository userRepository;

    /**
     * 认证服务  登录
     * 验证用户名密码 返回 jwt 和 refreshToken
     * @param command 登录有关所有参数集合
     * @return jwt code msg
     */
    public LoginResponse login(LoginRequest command) {

        // 校验 参数
        validateLoginCommand(command);

        // 与数据库比对密码
        User user = validatePassword(command);

        // 生成jwt
        String jwtToken = jwtTokenProvider.generateToken(user);

        // 返回
        return new LoginResponse(jwtToken, "登录成功");
    }

    /**
     * 验证登录命令
     * @param command 登录有关所有参数集合
     */
    public void validateLoginCommand(LoginRequest command) {
        if(command.getUsername() == null || command.getPassword() == null){
            throw new ServiceException(ExceptionCode.MISSING_ARGUMENT);
        }
    }

    /**
     * 匹配密码
     * @param command 登录有关所有参数集合
     * @return 参数有误或者用户密码不匹配 抛出异常/ 正确登录 返回用户
     */
    public User validatePassword(LoginRequest command) {
        User currentUser = userRepository.findByUsername(command.getUsername());
        if(currentUser == null){
            throw new ServiceException(ExceptionCode.USER_NOT_EXIST);
        }
        if(!currentUser.verifyPassword(command.getPassword())){
            throw new ServiceException(ExceptionCode.USER_ERROR_PASSWORD);
        }
        return currentUser;
    }

}
