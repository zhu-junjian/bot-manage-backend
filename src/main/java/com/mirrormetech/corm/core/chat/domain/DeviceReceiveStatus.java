package com.mirrormetech.corm.core.chat.domain;

import lombok.Getter;

/**
 * 设备接收状态
 * 设备接收状态 0-非设备接收消息 1-未接收 2-接收，未播放 3-接收，已播放
 */
@Getter
public enum DeviceReceiveStatus {

    NON_DEVICE(0,"非设备接收消息"),

    NON_RECEIVER(1, "未接收"),

    RECEIVED(2, "已接受"),

    PLAYED(3, "接收并播放")
    
    ;

    DeviceReceiveStatus(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }

    private Integer code;

    private String desc;
}
