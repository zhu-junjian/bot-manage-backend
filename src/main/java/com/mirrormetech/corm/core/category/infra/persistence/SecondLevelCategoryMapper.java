package com.mirrormetech.corm.core.category.infra.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mirrormetech.corm.core.category.infra.DO.SecondLevelCategoryDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SecondLevelCategoryMapper extends BaseMapper<SecondLevelCategoryDO> {

    List<SecondLevelCategoryDO> findByParentId(@Param("parentId") Long parentId);

}
