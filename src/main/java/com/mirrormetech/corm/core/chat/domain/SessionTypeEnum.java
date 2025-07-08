package com.mirrormetech.corm.core.chat.domain;

import lombok.Getter;

@Getter
public enum SessionTypeEnum {

    USER_TO_USER(0, "用户发送至用户"),
    USER_TO_DEVICE(1, "用户发送至设备");

    SessionTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Getter
    private Integer code;

    @Getter
    private String desc;
}
