package com.mirrormetech.corm.core.banner.domain.dto;

import lombok.Data;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Data
public class BannerDTO {

    private String name;

    private String heading;

    private String subheading;

    private String backgroundUrl;

    private String targetUrl;

    private int bannerType;

    private int weight;
}
