package com.mirrormetech.corm.core.category.infra.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mirrormetech.corm.core.category.infra.DO.FirstLevelCategoryDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FirstLevelCategoryMapper extends BaseMapper<FirstLevelCategoryDO> {

    List<FirstLevelCategoryDO> selectAll();

}
