package com.mirrormetech.corm.core.category.domain.repository;

import com.mirrormetech.corm.core.category.domain.FirstLevelCategory;
import com.mirrormetech.corm.core.category.domain.dto.CategoryDTO;
import com.mirrormetech.corm.core.category.infra.DO.FirstLevelCategoryDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author spencer
 * @date 2025/05/08
 */
public interface FirstLevelCategoryRepository {

    FirstLevelCategoryDO selectById(@Param("id") Long id);

    void insert(FirstLevelCategoryDO firstLevelCategoryDO);

    void save(CategoryDTO categoryDTO);

    List<FirstLevelCategoryDO> selectAllActive();
}
