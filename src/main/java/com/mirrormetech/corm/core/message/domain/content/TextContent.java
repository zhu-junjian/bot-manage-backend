package com.mirrormetech.corm.core.message.domain.content;

import com.mirrormetech.corm.core.message.domain.MessageTypeEnum;
import lombok.Data;

/**
 * messageType 纯文本
 * 纯文本消息的 内容
 */
@Data
public class TextContent implements Content{

    private String text;

    @Override
    public Integer messageType() {
        return MessageTypeEnum.TEXT.getCode();
    }
}
