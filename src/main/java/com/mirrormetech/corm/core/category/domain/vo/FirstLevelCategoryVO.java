package com.mirrormetech.corm.core.category.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Data
public class FirstLevelCategoryVO {

    /**
     * 大类主键ID
     */
    private Long id;

    /**
     * 大类名称
     */
    private String name;

    /**
     * 二级大类列表
     * 一级大类下的 二级大类
     */
    private List<SecondLevelCategoryVO> secondLevelCategories;
}
