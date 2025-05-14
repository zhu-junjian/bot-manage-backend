package com.mirrormetech.corm.core.device.infra.DO;


import lombok.Getter;

/**
 * 推文可见状态
 */
@Getter
public enum PostVisibility {

    ONLY_MYSELF(0, "仅自己可见"),
    ONLY_FRIEND(1, "仅好友可见"),
    PUBLIC(2, "公开");
    @Getter
    private final int code;

    private final String name;

    PostVisibility(int i, String name) {
        this.code = i;
        this.name = name;
    }

}
