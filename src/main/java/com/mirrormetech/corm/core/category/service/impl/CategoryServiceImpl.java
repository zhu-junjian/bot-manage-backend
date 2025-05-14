package com.mirrormetech.corm.core.category.service.impl;

import com.mirrormetech.corm.core.category.domain.CategoryDomainService;
import com.mirrormetech.corm.core.category.domain.dto.CategoryDTO;
import com.mirrormetech.corm.core.category.domain.vo.FirstLevelCategoryVO;
import com.mirrormetech.corm.core.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDomainService categoryDomainService;

    /**
     * 新增一级 或 二级 大类
     * @param categoryDTO 一次请求命令
     */
    public void save(CategoryDTO categoryDTO) {
        categoryDomainService.create(categoryDTO);
    }

    /**
     * 查询所有大类父子关系
     * @return 一级大类 二级大类列表
     */
    public List<FirstLevelCategoryVO> getAllCategories() {
        return categoryDomainService.findAll();
    }
}
