package com.mirrormetech.corm.core.like.infra.persistence;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mirrormetech.corm.core.like.domain.PostLike;
import com.mirrormetech.corm.core.like.domain.PostLikeFactory;
import com.mirrormetech.corm.core.like.domain.dto.PostLikeDTO;
import com.mirrormetech.corm.core.like.domain.dto.QueryPostLikeDTO;
import com.mirrormetech.corm.core.like.domain.repository.PostLikeRepository;
import com.mirrormetech.corm.core.like.infra.DO.PostLikeDO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author spencer
 * @date 2025/05/07
 */
@Repository
@RequiredArgsConstructor
public class MyBatisPostLikeRepository implements PostLikeRepository {

    private final PostLikeMapper postLikeMapper;

    private final PostLikeFactory postLikeFactory;

    @Override
    public List<PostLikeDO> selectList(QueryWrapper<PostLikeDO> wrapper) {
        return postLikeMapper.selectList(wrapper);
    }

    /**
     * 通过user_id & post_id的唯一键 判断status = 1-status
     * @param postLikeDTO
     * @return
     */
    @Override
    public int likeOrUnlike(PostLikeDTO postLikeDTO) {
        return postLikeMapper.likeOrUnlike(postLikeDTO.getUserId(), postLikeDTO.getPostId());
    }

    @Override
    public PostLikeDO findByPostId(QueryPostLikeDTO queryPostLikeDTO) {
        return postLikeMapper.selectOne(new QueryWrapper<PostLikeDO>()
                .eq("user_id", queryPostLikeDTO.getUserId())
                .eq("post_id",queryPostLikeDTO.getPostId()));
    }

    @Override
    public void save(PostLikeDO postLikeDO) {
        postLikeMapper.insert(postLikeDO);
    }

    /**
     * 根据点赞Id更新点赞记录  id为空会更新失败
     * @param postLikeDO 点赞实体
     */
    @Override
    public void update(PostLikeDO postLikeDO) {
        postLikeMapper.updateById(postLikeDO);
    }

    /**
     * 根据userId postId status查询点赞信息
     * @param postLikeDTO userId postId status
     * @return 点赞记录
     */
    @Override
    public Optional<PostLike> findByUserAndPost(PostLikeDTO postLikeDTO) {
        PostLikeDO postLikeDO = postLikeMapper.selectOne(new QueryWrapper<PostLikeDO>()
                .eq(postLikeDTO.getUserId()!= null,"user_id", postLikeDTO.getUserId())
                .eq(postLikeDTO.getPostId()!= null,"post_id", postLikeDTO.getPostId())
                .eq(postLikeDTO.getStatus()!= null, "status", postLikeDTO.getStatus())
        );
        PostLike postLike = postLikeFactory.getPostLikeByDO(postLikeDO);
        return Optional.ofNullable(postLike);
    }

    /**
     * 查询用户所有作品被点赞数
     * @param userId 目标用户ID
     * @return 被点赞数
     */
    @Override
    public Integer getUserWorksLikeCount(Long userId) {
        return postLikeMapper.userWorksLike(userId);
    }
}
