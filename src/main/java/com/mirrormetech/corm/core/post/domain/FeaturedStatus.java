package com.mirrormetech.corm.core.post.domain;

import lombok.Getter;

/**
 * 是否精选枚举类
 */
@Getter
public enum FeaturedStatus {

    /**
     * 未精选
     */
    UN_FEATURED(0),

    /**
     * 已精选
     */
    FEATURED(1);

    private FeaturedStatus(Integer code){
        this.code = code;
    }
    private Integer code;
}
