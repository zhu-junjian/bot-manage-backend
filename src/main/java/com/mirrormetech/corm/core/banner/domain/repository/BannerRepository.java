package com.mirrormetech.corm.core.banner.domain.repository;

import com.mirrormetech.corm.core.banner.infra.DO.BannerDO;

import java.util.List;

public interface BannerRepository {

    void save(BannerDO bannerDO);

    List<BannerDO> findAll();
}
