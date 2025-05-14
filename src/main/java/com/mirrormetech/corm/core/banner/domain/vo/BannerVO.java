package com.mirrormetech.corm.core.banner.domain.vo;

import lombok.Data;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Data
public class BannerVO {

    private String name;

    private String heading;

    private String subheading;

    private String backgroundUrl;

    private String targetUrl;

    private int bannerType;

    private int weight;
}
