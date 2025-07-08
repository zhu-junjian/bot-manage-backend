package com.mirrormetech.corm.core.message.domain.content;

import com.mirrormetech.corm.core.message.domain.MessageTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * 手信消息的内容
 */
@Data
public class HandMsgContent implements Content{

    private String text;

    private List<HandMsg> modules;

    @Override
    public Integer messageType() {
        return MessageTypeEnum.HAND_MSG.getCode();
    }
}
