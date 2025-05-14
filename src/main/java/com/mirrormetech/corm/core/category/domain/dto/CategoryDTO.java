package com.mirrormetech.corm.core.category.domain.dto;

import lombok.Data;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Data
public class CategoryDTO {

    private int categoryLevel;

    private Long firstLevelCategoryId;

    private String categoryName;
}
