package com.mirrormetech.corm.core.category.service;

import com.mirrormetech.corm.core.category.domain.dto.CategoryDTO;
import com.mirrormetech.corm.core.category.domain.vo.FirstLevelCategoryVO;

import java.util.List;

public interface CategoryService {

    void save(CategoryDTO categoryDTO);

    List<FirstLevelCategoryVO> getAllCategories();
}
