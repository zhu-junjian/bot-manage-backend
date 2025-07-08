package com.mirrormetech.corm.core.message.domain.content;

import com.mirrormetech.corm.core.message.domain.MessageTypeEnum;
import lombok.Data;

/**
 * 图文消息文本内容
 */
@Data
public class ImgTextContent implements Content{

    // 标题
    private String title;

    // 文本
    private String text;

    // 跳转文本
    private String jumpText;

    // 跳转URL
    private String jumpUrl;

    // 图片URL
    private String imageUrl;

    @Override
    public Integer messageType() {
        return MessageTypeEnum.IMG_TEXT.getCode();
    }
}
