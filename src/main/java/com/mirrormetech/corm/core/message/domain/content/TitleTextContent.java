package com.mirrormetech.corm.core.message.domain.content;

import com.mirrormetech.corm.core.message.domain.MessageTypeEnum;
import lombok.Data;

/**
 * 标题消息
 */
@Data
public class TitleTextContent implements Content{

    private String title;

    private String text;

    @Override
    public Integer messageType() {
        return MessageTypeEnum.TITLE_TEXT.getCode();
    }
}
