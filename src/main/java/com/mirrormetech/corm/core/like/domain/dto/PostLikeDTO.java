package com.mirrormetech.corm.core.like.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author spencer
 * @date 2025/05/07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostLikeDTO {

    private Long id;

    private Long userId;

    private Long postId;

    private Integer status;
}
