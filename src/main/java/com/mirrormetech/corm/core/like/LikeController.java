package com.mirrormetech.corm.core.like;

import com.mirrormetech.corm.common.result.ApiResult;
import com.mirrormetech.corm.core.like.domain.dto.PostLikeDTO;
import com.mirrormetech.corm.core.like.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * TODO 接口限流 request was banned
 * @author spencer
 * @date 2025/05/06
 */
@RestController
@RequestMapping("/api/v1/posts/like")
@RequiredArgsConstructor
public class LikeController {

    @Qualifier("postLikeServiceImpl")
    private final PostLikeService postLikeService;

    /**
     * TODO 限流
     * @param postLikeDTO
     * @return
     */
    @PostMapping
    public ApiResult like(@RequestBody PostLikeDTO postLikeDTO) {
        postLikeService.likeOrUnlike(postLikeDTO);
        return ApiResult.success();
    }
}
