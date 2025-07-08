package com.mirrormetech.corm.core.message.domain;

import lombok.Getter;

/**
 * 消息类型
 */
@Getter
public enum MessageTypeEnum {

    IMG_TEXT(1, "图文消息"),
    HAND_MSG(2, "手信消息"),
    TEXT(3,"纯文本"),
    TITLE_TEXT(4,"标题-文本"),
    LIKE_MSG(5,"点赞消息"),
    FOLLOWING_MSG(6,"关注消息")
    ;

    MessageTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    private Integer code;
    private String desc;
}
