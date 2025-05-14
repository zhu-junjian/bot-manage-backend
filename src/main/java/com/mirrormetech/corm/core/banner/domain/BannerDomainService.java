package com.mirrormetech.corm.core.banner.domain;

import com.mirrormetech.corm.core.banner.domain.dto.BannerDTO;
import com.mirrormetech.corm.core.banner.domain.repository.BannerRepository;
import com.mirrormetech.corm.core.banner.domain.vo.BannerVO;
import com.mirrormetech.corm.core.banner.infra.DO.BannerDO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Service
@RequiredArgsConstructor
public class BannerDomainService {

    @Qualifier("myBatisBannerRepository")
    private final BannerRepository myBatisBannerRepository;

    private final BannerFactory bannerFactory;

    public void save(BannerDTO bannerDTO) {
        myBatisBannerRepository.save(bannerFactory.create(bannerDTO));
    }

    public List<BannerVO> findAll() {
        List<BannerDO> bannerDOList = myBatisBannerRepository.findAll();
        return bannerFactory.createVOListByDO(bannerDOList);
    }

}
