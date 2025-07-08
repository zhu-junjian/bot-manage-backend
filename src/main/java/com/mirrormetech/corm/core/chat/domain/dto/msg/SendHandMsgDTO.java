package com.mirrormetech.corm.core.chat.domain.dto.msg;

import com.alibaba.fastjson.JSON;
import com.mirrormetech.corm.core.message.domain.MessageTypeEnum;
import com.mirrormetech.corm.core.message.domain.content.HandMsg;
import com.mirrormetech.corm.core.message.domain.content.HandMsgContent;
import lombok.Data;

import java.util.List;

@Data
public class SendHandMsgDTO extends BaseMsgDTO {

    // 当receiverType 为 设备时 需要填写设备Id
    private Long deviceId;

    // 0- 用户 1- 设备
    private Integer receiverType;

    private String text;

    private List<HandMsg> modules;

    @Override
    public Integer getMessageType(){
        return MessageTypeEnum.HAND_MSG.getCode();
    }

    @Override
    public String getContent() {
        HandMsgContent content = new HandMsgContent();
        content.setModules(modules);
        content.setText(text);
        return JSON.toJSONString(content);
    }
}
