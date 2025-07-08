package com.mirrormetech.corm.core.user.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 关注关系
 */
@Data
@NoArgsConstructor
public class FollowUserDTO {
    /**
     * 源用户ID
     */
    private Long sourceUserId;

    /**
     * 目标用户ID
     */
    private Long targetUserId;

    public FollowUserDTO(Long sourceUserId, Long targetUserId) {
        this.sourceUserId = sourceUserId;
        this.targetUserId = targetUserId;
    }
}
