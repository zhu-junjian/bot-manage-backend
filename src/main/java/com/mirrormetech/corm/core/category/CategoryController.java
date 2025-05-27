package com.mirrormetech.corm.core.category;

import com.mirrormetech.corm.common.result.ApiResult;
import com.mirrormetech.corm.core.category.domain.dto.CategoryDTO;
import com.mirrormetech.corm.core.category.domain.vo.FirstLevelCategoryVO;
import com.mirrormetech.corm.core.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author spencer
 * @date 2025/05/06
 */
@RestController
@RequestMapping("/api/v1/posts/category")
@RequiredArgsConstructor
public class CategoryController {

    @Qualifier("categoryServiceImpl")
    private final CategoryService categoryService;

    /**
     * 暂时不考虑列表添加
     * @param categoryDTO 单次请求内容
     */
    @PostMapping()
    public ApiResult createCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.save(categoryDTO);
        return ApiResult.success();
    }

    /**
     * 查询所有大类信息
     */
    @GetMapping
    public ApiResult<List<FirstLevelCategoryVO>> getAllCategories() {
        List<FirstLevelCategoryVO> firstLevelCategory = categoryService.getAllCategories();
        return ApiResult.success(firstLevelCategory);
    }
}
