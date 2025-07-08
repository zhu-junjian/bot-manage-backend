package com.mirrormetech.corm.core.like.domain;

import com.mirrormetech.corm.core.like.domain.dto.PostLikeDTO;
import com.mirrormetech.corm.core.like.infra.DO.PostLikeDO;
import com.mirrormetech.corm.core.post.domain.Post;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author spencer
 * @date 2025/05/07
 */
@Component
public class PostLikeFactory {

    /**
     * 将DO转换为PostLike
     * @param postLikeDO DO
     * @return PostLike
     */
    public PostLike getPostLikeByDO(PostLikeDO postLikeDO) {
        if (postLikeDO == null) {
            return null;
        }
        PostLike postLike = new PostLike();
        BeanUtils.copyProperties(postLikeDO, postLike);
        return postLike;
    }

    /**
     * 将DTO转换为DO 在点赞不存在时，由DTO封装DO
     * @param postLikeDTO 由控制层传入的点赞信息
     * @return 数据库存储对象
     */
    public PostLikeDO getDOByDTO(PostLikeDTO postLikeDTO) {
        if (postLikeDTO == null) {
            return null;
        }
        PostLikeDO postLikeDO = new PostLikeDO();
        BeanUtils.copyProperties(postLikeDTO, postLikeDO);
        return postLikeDO;
    }
}
