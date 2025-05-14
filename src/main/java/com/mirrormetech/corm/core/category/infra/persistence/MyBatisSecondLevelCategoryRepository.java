package com.mirrormetech.corm.core.category.infra.persistence;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mirrormetech.corm.core.category.domain.FirstLevelCategory;
import com.mirrormetech.corm.core.category.domain.SecondLevelCategory;
import com.mirrormetech.corm.core.category.domain.dto.CategoryDTO;
import com.mirrormetech.corm.core.category.domain.repository.FirstLevelCategoryRepository;
import com.mirrormetech.corm.core.category.domain.repository.SecondLevelCategoryRepository;
import com.mirrormetech.corm.core.category.infra.DO.SecondLevelCategoryDO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
@RequiredArgsConstructor
public class MyBatisSecondLevelCategoryRepository implements SecondLevelCategoryRepository {

    private final SecondLevelCategoryMapper secondLevelCategoryMapper;


    @Override
    public SecondLevelCategoryDO selectById(Long id) {
        return secondLevelCategoryMapper.selectById(id);
    }

    @Override
    public void insert(SecondLevelCategoryDO secondLevelCategoryDO) {
        secondLevelCategoryMapper.insert(secondLevelCategoryDO);
    }

    @Override
    public List<SecondLevelCategoryDO> findByParentId(@Param("parentId") Long parentId){
        return secondLevelCategoryMapper.findByParentId(parentId);
    }

    @Override
    public List<SecondLevelCategoryDO> findAll() {
        return secondLevelCategoryMapper.selectList(new QueryWrapper<>());
    }

}
