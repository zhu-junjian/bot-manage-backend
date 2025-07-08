package com.mirrormetech.corm.core.post.domain.dto;

import lombok.Data;

/**
 * 作品列表查询
 */
@Data
public class WorksQueryDTO {

    private Long sourceUserId;

    private Long targetUserId;

    private Integer page;

    private Integer size;

    public WorksQueryDTO(Long sourceUserId, Long targetUserId, Integer page, Integer size) {
        this.sourceUserId = sourceUserId;
        this.targetUserId = targetUserId;
        this.page = page;
        this.size = size;
    }
}
