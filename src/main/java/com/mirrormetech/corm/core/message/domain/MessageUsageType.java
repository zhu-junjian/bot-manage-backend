package com.mirrormetech.corm.core.message.domain;

import lombok.Getter;

/**
 * 消息使用者的类型
 * 消息发送方和接收方的类型
 * 0-对象为用户
 * 1-对象为设备
 */
@Getter
public enum MessageUsageType {

    USER(0, "用户类型"),
    DEVICE(1, "设备类型")
    ;

    private Integer code;

    private String desc;

    MessageUsageType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
