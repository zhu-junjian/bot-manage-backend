package com.mirrormetech.corm.core.user;

import com.mirrormetech.corm.common.result.ApiResult;
import com.mirrormetech.corm.core.user.domain.dto.FollowUserDTO;
import com.mirrormetech.corm.core.user.domain.vo.UserUpdateVO;
import com.mirrormetech.corm.core.user.domain.vo.UserVO;
import com.mirrormetech.corm.core.user.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**
 * 用户模块
 */
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    @Qualifier("userServiceImpl")
    private final UserService userService;

    /**
     * 新增用户
     */

    /**
     * 根据目标用户和源用户ID查询目标用户信息
     * 源用户ID主要用于区分是否与目标用户ID一致，不一致的情况下 不展示曝光量等信息
     * @param targetUserId 目标用户ID
     * @param sourceUserId 源用户ID
     * @return 单个用户信息 包含源用户与目标用户的关注关系
     */
    @GetMapping("/profile")
    public ApiResult<UserVO> getUserById(@RequestParam("targetUserId") @NotNull Long targetUserId,
                                       @RequestParam(value = "sourceUserId", required = false) Long sourceUserId) {
        UserVO vo = userService.getUserProfileWithRelation(new FollowUserDTO(sourceUserId, targetUserId));
        return ApiResult.success(vo);
    }

    /**
     *  修改用户个人资料
     *  注意： 字段不传入或者传入为null不会修改，传入为空值''会清空
     *  用户可以修改其自身
     *  管理员用户可以修改所有人
     * @param userId
     * @param vo
     * @return
     */
    @PutMapping("/profile/{userId}")
    public ApiResult<UserUpdateVO> updateUser(@PathVariable("userId") Long userId, @RequestBody UserUpdateVO vo) {
        UserUpdateVO updatedUserProfile = userService.updateUserProfile(vo, userId);
        return ApiResult.success(updatedUserProfile);
    }
}
