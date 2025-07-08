package com.mirrormetech.corm.core.user.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.core.user.domain.FollowRelation;
import com.mirrormetech.corm.core.user.domain.dto.UserFollowDTO;

import java.util.List;
import java.util.Map;

/**
 * 用户关注仓储层
 */
public interface UserFollowRepository {

    Map<Long, FollowRelation> batchGetRelations(Long sourceId, List<Long> targetIds);

    void followUser(Long followerId, Long followingId);

    void unfollowUser(Long followerId, Long followingId);

    Page<UserFollowDTO> getFollowings(Long userId, Page<UserFollowDTO> page);

    Page<UserFollowDTO> getFollowers(Long userId, Page<UserFollowDTO> page);

    FollowRelation getRelation(Long sourceId, Long targetId);
}
