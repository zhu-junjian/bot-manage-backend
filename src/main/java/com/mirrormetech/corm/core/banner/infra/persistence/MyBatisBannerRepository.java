package com.mirrormetech.corm.core.banner.infra.persistence;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mirrormetech.corm.core.banner.domain.dto.BannerDTO;
import com.mirrormetech.corm.core.banner.domain.repository.BannerRepository;
import com.mirrormetech.corm.core.banner.infra.DO.BannerDO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Repository
@RequiredArgsConstructor
public class MyBatisBannerRepository implements BannerRepository {

    private final BannerMapper bannerMapper;

    @Override
    public void save(BannerDO bannerDO) {
        bannerMapper.insert(bannerDO);
    }

    @Override
    public List<BannerDO> findAll() {
        return bannerMapper.selectList(new QueryWrapper<BannerDO>().eq("status", 0));
    }
}
