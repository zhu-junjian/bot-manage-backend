package com.mirrormetech.corm.core.banner.service.impl;

import com.mirrormetech.corm.core.banner.domain.BannerDomainService;
import com.mirrormetech.corm.core.banner.domain.dto.BannerDTO;
import com.mirrormetech.corm.core.banner.domain.repository.BannerRepository;
import com.mirrormetech.corm.core.banner.domain.vo.BannerVO;
import com.mirrormetech.corm.core.banner.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerDomainService bannerDomainService;

    public void save(BannerDTO bannerDTO) {
        bannerDomainService.save(bannerDTO);
    }

    public List<BannerVO> findAll() {
        return bannerDomainService.findAll();
    }
}
