package com.mirrormetech.corm.core.post.domain.dto;

import lombok.Data;

/**
 * 列表查询接口的 参数封装
 * @author spencer
 * @date 2025/05/07
 */
@Data
public class QueryListDTO {

    private Long slcId;

    private Long flcId;

    private Integer isFeatured;

    private Integer status;

    public QueryListDTO(Long slcId, Long flcId) {
        this.slcId = slcId;
        this.flcId = flcId;
    }
}
