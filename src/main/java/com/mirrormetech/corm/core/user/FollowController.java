package com.mirrormetech.corm.core.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.common.result.ApiResult;
import com.mirrormetech.corm.core.user.domain.FollowRelation;
import com.mirrormetech.corm.core.user.domain.dto.FollowUserDTO;
import com.mirrormetech.corm.core.user.domain.dto.UserFollowDTO;
import com.mirrormetech.corm.core.user.domain.vo.RelationVO;
import com.mirrormetech.corm.core.user.infra.persistence.MyBatisFollowRepoImpl;
import com.mirrormetech.corm.core.user.service.FollowService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**
 * 用户关注模块
 * TODO 业务层 domainService封装 解耦的越细 扩展越容易
 */
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class FollowController {

    private final MyBatisFollowRepoImpl followRepo;

    @Qualifier("followServiceImpl")
    private final FollowService followService;

    /**
     * 关注
     * @param followUserDTO sourceUserId -> 源用户ID  targetUserId -> 目标用户ID
     * @return 现关注关系
     */
    @PostMapping("/follow")
    public ApiResult<RelationVO> follow(@RequestBody FollowUserDTO followUserDTO) {
        followService.followUser(followUserDTO.getSourceUserId(), followUserDTO.getTargetUserId());
        FollowRelation relation = followRepo.getRelation(followUserDTO.getSourceUserId(), followUserDTO.getTargetUserId());
        return ApiResult.success(new RelationVO(followUserDTO.getSourceUserId(), followUserDTO.getTargetUserId(), relation.getCode()));
    }

    /**
     * 取消关注
     * @param followUserDTO sourceUserId -> 源用户ID  targetUserId -> 目标用户ID
     * @return 现关注关系
     */
    @PostMapping("/unfollow")
    public ApiResult<RelationVO> unfollow(@RequestBody FollowUserDTO followUserDTO) {
        followRepo.unfollowUser(followUserDTO.getSourceUserId(), followUserDTO.getTargetUserId());
        FollowRelation relation = followRepo.getRelation(followUserDTO.getSourceUserId(), followUserDTO.getTargetUserId());
        return ApiResult.success(new RelationVO(followUserDTO.getSourceUserId(), followUserDTO.getTargetUserId(), relation.getCode()));
    }

    @GetMapping("/follow/relation")
    public ApiResult<RelationVO> getRelation(@RequestParam @NotNull @Min(1) Long sourceUserId,
                                             @RequestParam @NotNull @Min(1) Long targetUserId) {
        FollowRelation relation = followRepo.getRelation(sourceUserId, targetUserId);
        return ApiResult.success(new RelationVO(sourceUserId, targetUserId, relation.getCode()));
    }

    /**
     * 关注列表-分页
     * @param targetUserId 目标用户ID
     * @param sourceUserId 源用户ID
     * @param page 页码
     * @param size 条数
     * @return 关注列表 page
     */
    @GetMapping("/followings")
    public ApiResult<Page<UserFollowDTO>> getFollowings(
            @RequestParam @NotNull Long targetUserId,
            @RequestParam Long sourceUserId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<UserFollowDTO> result = followRepo.getFollowingsWithRelation(sourceUserId, targetUserId, new Page<>(page, size));
        return ApiResult.success(result);
    }

    /**
     * 粉丝列表-分页
     * @param targetUserId 目标用户ID
     * @param sourceUserId 源用户ID
     * @param page 页码
     * @param size 条数
     * @return 粉丝列表 page
     */
    @GetMapping("/followers")
    public ApiResult<Page<UserFollowDTO>> getFollowers(
            @RequestParam @NotNull Long targetUserId,
            @RequestParam Long sourceUserId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<UserFollowDTO> result = followRepo.getFollowersWithRelation(sourceUserId, targetUserId, new Page<>(page, size));
        return ApiResult.success(result);
    }
}
