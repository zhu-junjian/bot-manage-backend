package com.mirrormetech.corm.core.message.domain;

import lombok.Getter;

/**
 * 消息源
 */
@Getter
public enum MessageSource {

    OPS(0, "运营平台"),
    APP(1, "手机APP");

    private Integer code;

    private String desc;

    MessageSource(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
