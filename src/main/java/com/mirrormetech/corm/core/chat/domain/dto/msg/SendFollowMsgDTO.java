package com.mirrormetech.corm.core.chat.domain.dto.msg;

import com.mirrormetech.corm.core.message.domain.MessageTypeEnum;
import lombok.Data;

@Data
public class SendFollowMsgDTO extends BaseMsgDTO {

    private Integer receiverType;

    @Override
    public Integer getMessageType(){
        return MessageTypeEnum.FOLLOWING_MSG.getCode();
    }

    @Override
    public String getContent() {
        return "";
    }
}
