package com.mirrormetech.corm.core.like.infra.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mirrormetech.corm.core.like.infra.DO.PostLikeDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author spencer
 * @date 2025/05/07
 */
@Mapper
public interface PostLikeMapper extends BaseMapper<PostLikeDO> {

    @Insert(
            "INSERT INTO user_post_like ( user_id, post_id) " +
            "VALUES (${userId}, ${postId}) " +
            "ON DUPLICATE KEY UPDATE " +
            "STATUS = 1- STATUS,like_time = NOW()"
    )
    int likeOrUnlike(@Param("userId") Long userId, @Param("postId") Long postId);
}
