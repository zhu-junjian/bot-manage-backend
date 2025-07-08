package com.mirrormetech.corm.core.user.service;

import com.mirrormetech.corm.core.user.domain.dto.FollowUserDTO;
import com.mirrormetech.corm.core.user.domain.vo.UserUpdateVO;
import com.mirrormetech.corm.core.user.domain.vo.UserVO;

public interface UserService {

    UserVO getUserProfileWithRelation(FollowUserDTO followUserDTO);

    UserUpdateVO updateUserProfile(UserUpdateVO userUpdateVO, Long userId);
}
