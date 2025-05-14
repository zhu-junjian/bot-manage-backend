package com.mirrormetech.corm.core.banner.service;

import com.mirrormetech.corm.core.banner.domain.dto.BannerDTO;
import com.mirrormetech.corm.core.banner.domain.vo.BannerVO;

import java.util.List;

/**
 * @author spencer
 * @date 2025/05/08
 */
public interface BannerService {

    void save(BannerDTO bannerDTO);

    List<BannerVO> findAll();
}
