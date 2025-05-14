package com.mirrormetech.corm.core.banner;

import com.mirrormetech.corm.common.result.ApiResult;
import com.mirrormetech.corm.core.banner.domain.dto.BannerDTO;
import com.mirrormetech.corm.core.banner.domain.vo.BannerVO;
import com.mirrormetech.corm.core.banner.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author spencer
 * @date 2025/05/06
 */
@RestController
@RequestMapping("/api/banner")
@RequiredArgsConstructor
public class BannerController {

    @Qualifier("bannerServiceImpl")
    private final BannerService bannerServiceImpl;

    @PostMapping
    public ApiResult createBanner(@RequestBody BannerDTO bannerDTO) {
        bannerServiceImpl.save(bannerDTO);
        return ApiResult.success();
    }

    @GetMapping
    public ApiResult<List<BannerVO>> getBanners() {
        List<BannerVO> banners = bannerServiceImpl.findAll();
        return ApiResult.success(banners);
    }
}
