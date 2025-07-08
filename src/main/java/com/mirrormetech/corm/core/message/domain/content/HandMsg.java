package com.mirrormetech.corm.core.message.domain.content;

import lombok.Data;

/**
 * 手信消息content中modules内容
 */
@Data
public class HandMsg {

    private Long userId;

    private String nickName;

    private String avatarUrl;

    private String content;

    private String imageUrl;

    private String audioUrl;

    private String actionUrl;

}
