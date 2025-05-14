package com.mirrormetech.corm.core.category.infra.persistence;

import com.mirrormetech.corm.core.category.domain.FirstLevelCategory;
import com.mirrormetech.corm.core.category.domain.dto.CategoryDTO;
import com.mirrormetech.corm.core.category.domain.repository.FirstLevelCategoryRepository;
import com.mirrormetech.corm.core.category.infra.DO.FirstLevelCategoryDO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
@RequiredArgsConstructor
public class MyBatisFirstLevelCategoryRepository implements FirstLevelCategoryRepository {

    private final FirstLevelCategoryMapper firstLevelCategoryMapper;

    @Override
    public FirstLevelCategoryDO selectById(Long id) {
        return firstLevelCategoryMapper.selectById(id);
    }

    @Override
    public void insert(FirstLevelCategoryDO firstLevelCategoryDO) {
        firstLevelCategoryMapper.insert(firstLevelCategoryDO);
    }

    @Override
    public void save(CategoryDTO categoryDTO) {

    }

    @Override
    public List<FirstLevelCategoryDO> selectAll(){
        return firstLevelCategoryMapper.selectAll();
    }

}
