package com.mirrormetech.corm.core.like.domain;

import com.mirrormetech.corm.core.like.domain.dto.PostLikeDTO;
import com.mirrormetech.corm.core.like.domain.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 用户点赞 - 领域服务
 * @author spencer
 * @date 2025/05/07
 */
@Service
@RequiredArgsConstructor
public class PostLikeDomainService {

    @Qualifier("myBatisPostLikeRepository")
    private final PostLikeRepository mybatisPostLikeRepository;

    public int likeOrUnlike(PostLikeDTO postLikeDTO) {
        return mybatisPostLikeRepository.likeOrUnlike(postLikeDTO);
    }
}
