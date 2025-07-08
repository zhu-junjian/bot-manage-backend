package com.mirrormetech.corm.core.like.infra.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mirrormetech.corm.core.like.infra.DO.PostLikeDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    @Select("SELECT" +
            "  COUNT( upl.id ) AS total_likes " +
            "FROM" +
            "  post_record pr" +
            "  LEFT JOIN user_post_like upl ON pr.id = upl.post_id" +
            "  AND upl.STATUS = 0" +
            " WHERE pr.user_id = #{userId}")
    Integer userWorksLike(@Param("userId") Long userId);
}
