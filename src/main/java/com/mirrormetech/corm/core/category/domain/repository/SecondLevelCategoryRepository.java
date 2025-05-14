package com.mirrormetech.corm.core.category.domain.repository;

import com.mirrormetech.corm.core.category.infra.DO.SecondLevelCategoryDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SecondLevelCategoryRepository {

    SecondLevelCategoryDO selectById(@Param("id") Long id);

    void insert(SecondLevelCategoryDO secondLevelCategoryDO);

    List<SecondLevelCategoryDO> findByParentId(@Param("parentId") Long parentId);

    List<SecondLevelCategoryDO> findAll();
}
