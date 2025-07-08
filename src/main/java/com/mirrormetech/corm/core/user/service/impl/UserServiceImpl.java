package com.mirrormetech.corm.core.user.service.impl;

import com.mirrormetech.corm.core.user.domain.UserDomainService;
import com.mirrormetech.corm.core.user.domain.dto.FollowUserDTO;
import com.mirrormetech.corm.core.user.domain.vo.UserUpdateVO;
import com.mirrormetech.corm.core.user.domain.vo.UserVO;
import com.mirrormetech.corm.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 应用层（粗粒度接口的编排） -> 领域服务（跨领域能力组合编排，暴露粗力度接口开放给应用层） ->领域模型（领域能力）
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDomainService userDomainService;

    /**
     * 更新用户资料
     * @param userUpdateVO 修改内容
     */
    public UserUpdateVO updateUserProfile(UserUpdateVO userUpdateVO, Long userId) {
        userUpdateVO.setId(userId);
        return userDomainService.updateUserProfile(userUpdateVO);
    }

    /**
     * 查询用户个人资料页
     * @param followUserDTO 源用户ID & 目标用户ID
     * @return 源用户视角下的  目标用户个人资料页 含关注关系
     */
    public UserVO getUserProfileWithRelation(FollowUserDTO followUserDTO) {
        return userDomainService.getUserProfileWithRelation(followUserDTO);
    }
}
