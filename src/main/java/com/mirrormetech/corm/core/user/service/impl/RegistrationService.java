package com.mirrormetech.corm.core.user.service.impl;


import com.mirrormetech.corm.common.exception.PhoneAlreadyRegisteredException;
import com.mirrormetech.corm.common.exception.UserErrorMessage;
import com.mirrormetech.corm.core.user.domain.RegistrationEvent;
import com.mirrormetech.corm.core.user.domain.repository.UserRepository;
import com.mirrormetech.corm.core.user.domain.vo.RegistrationRequest;
import com.mirrormetech.corm.core.user.infra.User;
import org.springframework.stereotype.Service;

/**
 * 注册  应用服务
 * 防御性编程
 * @author spencer
 * @date 2025/04/18
 */
@Service
public class RegistrationService {

    private final UserRepository userRepository;

    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registration(RegistrationRequest registrationRequest){

        //校验 用户名/手机号 唯一性
        validateRegistrationInfo(registrationRequest);

        //生成用户实体
        User user = createUser(registrationRequest);

        // 新用户触发事件  推送广告或者欢迎词到消息队列  此处只定义内容对象 不涉及具体字段 保证解耦 可扩展
        publishRegistrationEvent(new RegistrationEvent());

        // 入库
        userRepository.insertUser(user);
    }

    /**
     * 校验 用户注册信息
     * @param registrationRequest 注册对象
     */
    private void validateRegistrationInfo(RegistrationRequest registrationRequest){
        if (userRepository.findByUsername(registrationRequest.getUsername()) != null) {
            throw new PhoneAlreadyRegisteredException(UserErrorMessage.USER_NAME_EXISTS.getCode(),UserErrorMessage.USER_NAME_EXISTS.getMessage(), null);
        }
    }

    /**
     * 生成 用户实体
     */
    private User createUser(RegistrationRequest registrationRequest){
        String username = registrationRequest.getUsername();
        String password = registrationRequest.getPassword();
        return User.create(username, password);
    }

    private void publishRegistrationEvent(RegistrationEvent registrationEvent){
        // TODO 新用户创建事件
    }
}
