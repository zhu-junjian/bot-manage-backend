package com.mirrormetech.corm.core.like.service.impl;

import com.mirrormetech.corm.core.like.domain.PostLikeDomainService;
import com.mirrormetech.corm.core.like.domain.dto.PostLikeDTO;
import com.mirrormetech.corm.core.like.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 应用层 对 领域能力编排
 * @author spencer
 * @date 2025/05/07
 */
@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeDomainService postLikeDomainService;

    @Override
    public void likeOrUnlike(PostLikeDTO postLikeDTO) {
        postLikeDomainService.likeOrUnlike(postLikeDTO);
    }

    public void like(PostLikeDTO postLikeDTO) {}

    public void unlike(PostLikeDTO postLikeDTO) {}
}
