package com.mirrormetech.corm.core.post.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 列表查询接口的 参数封装
 * @author spencer
 * @date 2025/05/07
 */
@Data
@AllArgsConstructor
public class QueryListDTO {

    private Long slcId;

    private Long flcId;
}
