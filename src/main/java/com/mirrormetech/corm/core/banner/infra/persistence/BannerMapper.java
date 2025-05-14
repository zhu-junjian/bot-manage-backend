package com.mirrormetech.corm.core.banner.infra.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mirrormetech.corm.core.banner.infra.DO.BannerDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Repository
@Mapper
public interface BannerMapper extends BaseMapper<BannerDO> {
}
