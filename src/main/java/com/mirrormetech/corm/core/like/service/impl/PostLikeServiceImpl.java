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

    /**
     * 此处是postLike的应用层
     * 主要能力为负责用户点赞域的业务逻辑
     *   用户点赞所触发的领域事件或者说，用户点赞所带来的新的业务逻辑发送点赞消息 应该也属于这个领域内的事件 需要在用户点在的领域服务中对消息域的逻辑进行组合编排
     * @param postLikeDTO
     * @return
     */
    @Override
    public String likeOrUnlike(PostLikeDTO postLikeDTO) {
        return postLikeDomainService.likeOrUnlike(postLikeDTO);
    }

    public void like(PostLikeDTO postLikeDTO) {}

    public void unlike(PostLikeDTO postLikeDTO) {}
}
