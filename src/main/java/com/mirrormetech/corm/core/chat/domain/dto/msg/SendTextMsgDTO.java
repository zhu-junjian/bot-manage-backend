package com.mirrormetech.corm.core.chat.domain.dto.msg;

import lombok.Data;

/**
 * 纯文本消息传输对象
 */
@Data
public class SendTextMsgDTO extends BaseMsgDTO {

    private Integer receiverType;

    @Override
    public Integer getMessageType(){
        return 3;
    }

    @Override
    public String getContent() {
        return text;
    }

    private String text;
}
