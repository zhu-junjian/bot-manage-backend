package com.mirrormetech.corm.core.post.domain;

import lombok.Getter;

/**
 * 内容 作品 状态枚举类
 */
@Getter
public enum PostStatus {

    //正常
    NORMAL(0),

    //禁用
    BLOCKED(1),

    //删除
    REMOVED(2),

    //官方 删除
    OFFICIAL_REMOVED(3),

    //官方 禁用
    OFFICIAL_BLOCKED(4);

    private PostStatus(Integer code) {
        this.code = code;
    }
    private Integer code;
}
