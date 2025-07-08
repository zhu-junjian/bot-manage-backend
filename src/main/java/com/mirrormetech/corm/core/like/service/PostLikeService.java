package com.mirrormetech.corm.core.like.service;

import com.mirrormetech.corm.core.like.domain.dto.PostLikeDTO;

public interface PostLikeService {

    String likeOrUnlike(PostLikeDTO postLikeDTO);
}
