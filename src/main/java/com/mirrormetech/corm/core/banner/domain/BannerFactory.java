package com.mirrormetech.corm.core.banner.domain;

import com.mirrormetech.corm.core.banner.domain.dto.BannerDTO;
import com.mirrormetech.corm.core.banner.domain.vo.BannerVO;
import com.mirrormetech.corm.core.banner.infra.DO.BannerDO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Component
public class BannerFactory {

    public BannerDO create(BannerDTO bannerDTO) {
        BannerDO bannerDO = new BannerDO();
        BeanUtils.copyProperties(bannerDTO, bannerDO);
        return bannerDO;
    }

    public List<BannerVO> createVOListByDO(List<BannerDO> bannerDOList) {
        List<BannerVO> bannerVOList = new ArrayList<>();
        bannerDOList.forEach(bannerDO -> {
            BannerVO bannerVO = new BannerVO();
            BeanUtils.copyProperties(bannerDO, bannerVO);
            bannerVOList.add(bannerVO);
        });
        return bannerVOList;
    }
}
