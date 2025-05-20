package com.mirrormetech.corm.core.post.infra.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.core.post.infra.Do.PostDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author spencer
 * @date 2025/05/06
 */
@Repository
@Mapper
public interface PostMapper extends BaseMapper<PostDO> {

    Page<PostDO> selectPosts(Page<PostDO> page, @Param("firstLevelCategory") Long firstLevelCategory,
                             @Param("secondLevelCategory") Long secondLevelCategory);
}
