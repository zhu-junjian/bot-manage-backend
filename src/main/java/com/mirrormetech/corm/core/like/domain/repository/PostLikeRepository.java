package com.mirrormetech.corm.core.like.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mirrormetech.corm.core.like.domain.PostLike;
import com.mirrormetech.corm.core.like.domain.dto.PostLikeDTO;
import com.mirrormetech.corm.core.like.domain.dto.QueryPostLikeDTO;
import com.mirrormetech.corm.core.like.infra.DO.PostLikeDO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository {

    List<PostLikeDO> selectList(QueryWrapper<PostLikeDO> wrapper);

    int likeOrUnlike(PostLikeDTO postLikeDTO);

    PostLikeDO findByPostId(QueryPostLikeDTO queryPostLikeDTO);

    void save(PostLikeDO postLikeDO);

    void update(PostLikeDO postLikeDO);

    /**
     * 通过用户Id 和 内容Id查询是否点赞
     * @param userId 用户ID
     * @param postId 内容ID
     * @return
     */
    Optional<PostLike> findByUserAndPost(PostLikeDTO postLikeDTO);

    Integer getUserWorksLikeCount(Long userId);

}
