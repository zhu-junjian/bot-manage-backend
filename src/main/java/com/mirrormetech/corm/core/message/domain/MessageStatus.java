package com.mirrormetech.corm.core.message.domain;

import lombok.Getter;

@Getter
public enum MessageStatus {

    UNREAD(0, "未读"),
    READ(1, "已读");

    private Integer code;

    private String desc;

    private MessageStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
