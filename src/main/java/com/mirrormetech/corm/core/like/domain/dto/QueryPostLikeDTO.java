package com.mirrormetech.corm.core.like.domain.dto;

import lombok.Data;

/**
 * @author spencer
 * @date 2025/05/07
 */
@Data
public class QueryPostLikeDTO {

    private Long userId;

    private Long postId;

    private int status;
}
