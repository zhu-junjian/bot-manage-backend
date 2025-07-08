package com.mirrormetech.corm.core.chat.domain.dto.msg;

import lombok.Data;

@Data
public class SendLikeMsgDTO extends BaseMsgDTO {

    private Integer receiverType;

    @Override
    public Integer getMessageType(){
        return 5;
    }

    @Override
    public String getContent() {
        return "";
    }
}
