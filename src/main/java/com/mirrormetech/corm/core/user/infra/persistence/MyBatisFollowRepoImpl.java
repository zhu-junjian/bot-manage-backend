package com.mirrormetech.corm.core.user.infra.persistence;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.common.exception.ExceptionCode;
import com.mirrormetech.corm.common.exception.ServiceException;
import com.mirrormetech.corm.core.user.domain.FollowRelation;
import com.mirrormetech.corm.core.user.domain.dto.UserFollowDTO;
import com.mirrormetech.corm.core.user.domain.repository.UserFollowRepository;
import com.mirrormetech.corm.core.user.infra.DO.UserFollow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyBatisFollowRepoImpl implements UserFollowRepository {

    private final FollowMapper followMapper;

    /**
     * 获取关注列表（带用户信息和关系状态）
     * @param currentUserId 源用户ID（打开这个页面的用户ID）
     * @param targetUserId 目标用户ID（被查看的用户ID）
     * @param page 分页条件
     * @return 我关注的用户信息列表 支持分页
     */
    public Page<UserFollowDTO> getFollowingsWithRelation(Long currentUserId, Long targetUserId, Page<Object> page) {
        // 1. 获取关注列表（带用户信息）
        Page<UserFollowDTO> userPage = getFollowings(targetUserId, new Page<>(page.getCurrent(), page.getSize()));

        // 2. 批量获取关系状态
        List<Long> targetUserIds = userPage.getRecords().stream()
                .map(UserFollowDTO::getId)
                .collect(Collectors.toList());

        Map<Long, FollowRelation> relations = batchGetRelations(currentUserId, targetUserIds);

        // 3. 组合结果
        List<UserFollowDTO> resultList = userPage.getRecords().stream()
                //TODO Map Or Peek
                .peek(user -> {
                    FollowRelation relation = relations.getOrDefault(user.getId(), FollowRelation.NO_RELATION);
                    user.setRelation(relation.getCode());
                })
                .collect(Collectors.toList());

        // 4. 返回分页结果
        Page<UserFollowDTO> pageList = new Page<>(page.getCurrent(), page.getSize(), userPage.getTotal());
        pageList.setRecords(resultList);
        return pageList;
    }

    /**
     * 获取粉丝列表（带用户信息和关系状态）
     * @param currentUserId 源用户ID（打开这个页面的用户ID）
     * @param targetUserId 目标用户ID（被查看的用户ID）
     * @param page 分页条件
     * @return 目标用户的的粉丝信息列表 支持分页
     */
    public Page<UserFollowDTO> getFollowersWithRelation(Long currentUserId, Long targetUserId, Page<Object> page) {
        // 1. 获取粉丝列表（带用户信息）
        Page<UserFollowDTO> userPage = getFollowers(targetUserId, new Page<>(page.getCurrent(), page.getSize()));

        // 2. 批量获取关系状态
        List<Long> followerIds = userPage.getRecords().stream()
                .map(UserFollowDTO::getId)
                .collect(Collectors.toList());

        Map<Long, FollowRelation> relations = batchGetRelations(currentUserId, followerIds);

        // 3. 组合结果
        List<UserFollowDTO> resultList = userPage.getRecords().stream()
                .map(user -> {
                    FollowRelation relation = relations.getOrDefault(user.getId(), FollowRelation.NO_RELATION);
                    user.setRelation(relation.getCode());
                    return user;
                })
                .collect(Collectors.toList());

        // 4. 返回分页结果
        // 4. 返回分页结果
        Page<UserFollowDTO> pageList = new Page<>(page.getCurrent(), page.getSize(), userPage.getTotal());
        pageList.setRecords(resultList);
        return pageList;
    }

    /**
     * 批量查询用户ID与目标用户IDS的关注关系
     * @param sourceId 查询用户
     * @param targetIds 目标用户IDS集合
     * @return 用户与目标用户关注关系集合
     */
    @Override
    public Map<Long, FollowRelation> batchGetRelations(Long sourceId, List<Long> targetIds) {
        if (CollectionUtils.isEmpty(targetIds)) {
            return Collections.emptyMap();
        }

        // 1. 查询当前用户关注了哪些目标用户
        List<Long> followingIds = followMapper.findFollowingIds(sourceId, targetIds);
        Set<Long> followingSet = new HashSet<>(followingIds);

        // 2. 查询哪些目标用户关注了当前用户
        List<Long> followerIds = followMapper.findFollowerIds(sourceId, targetIds);
        Set<Long> followedBySet = new HashSet<>(followerIds);

        // 3. 构建关系映射
        Map<Long, FollowRelation> result = new HashMap<>();
        for (Long targetId : targetIds) {
            if(sourceId.equals(targetId)) {
                result.put(targetId, FollowRelation.SELF);
                break;
            }
            boolean isFollowing = followingSet.contains(targetId);
            boolean isFollowedBy = followedBySet.contains(targetId);

            if (isFollowing && isFollowedBy) {
                result.put(targetId, FollowRelation.MUTUAL);
            } else if (isFollowing) {
                result.put(targetId, FollowRelation.FOLLOWING);
            } else if (isFollowedBy) {
                result.put(targetId, FollowRelation.FOLLOWED_BY);
            } else {
                result.put(targetId, FollowRelation.NO_RELATION);
            }
        }

        return result;
    }

    /**
     * 关注用户
      * @param followerId 关注者ID
     * @param followingId 被关注者ID
     */
    @Transactional
    public void followUser(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            //throw new BusinessException("不能关注自己");
            throw new ServiceException(ExceptionCode.CANNOT_FOLLOW_YOURSELF);
        }

        if (followMapper.existsFollowRelation(followerId, followingId)) {
            //幂等处理，已存在关系直接返回
            throw new ServiceException(ExceptionCode.ALREADY_FOLLOWING);
        }

        UserFollow entity = new UserFollow();
        entity.setFollowerId(followerId);
        entity.setFollowingId(followingId);
        followMapper.insert(entity);
    }

    /**
     * 取消关注
      * @param followerId 关注者ID
     * @param followingId 被关注者ID
     */
    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        followMapper.deleteFollowRelation(followerId, followingId);
    }

    /**
     * 获取当前用户与目标用户的 关系状态
      * @param sourceId 当前用户
     * @param targetId 目标用户
     * @return 关系枚举
     */
    public FollowRelation getRelation(Long sourceId, Long targetId) {
        if(sourceId.equals(targetId)) return FollowRelation.SELF;
        boolean isFollowing = followMapper.existsFollowRelation(sourceId, targetId);
        boolean isFollowedBy = followMapper.existsFollowRelation(targetId, sourceId);

        if (isFollowing && isFollowedBy) return FollowRelation.MUTUAL;
        if (isFollowing) return FollowRelation.FOLLOWING;
        if (isFollowedBy) return FollowRelation.FOLLOWED_BY;
        return FollowRelation.NO_RELATION;
    }

    /**
     * 获取关注列表
     * @param userId 我
     * @param page 关注用户信息 分页
     * @return 关注用户分页集合
     */
    public Page<UserFollowDTO> getFollowings(Long userId, Page<UserFollowDTO> page) {
        return followMapper.selectFollowingIds(page, userId);
    }

    /**
     * 获取粉丝列表
     * @param userId 我
     * @param page 粉丝用户信息 分页
     * @return 分页集合
     */
    public Page<UserFollowDTO> getFollowers(Long userId, Page<UserFollowDTO> page) {
        return followMapper.selectFollowerIds(page, userId);
    }

    /**
     * 查询当前用户关注数
     * @param userId 当前用户ID
     * @return 关注数
     */
    public Integer getFollowingsCount(Long userId) {
        return followMapper.selectFollowingCount(userId);
    }

    /**
     * 查询当前用户粉丝数
     * @param userId 当前用户ID
     * @return 粉丝数
     */
    public Integer getFollowersCount(Long userId) {
        return followMapper.selectFollowerCount(userId);
    }
}
