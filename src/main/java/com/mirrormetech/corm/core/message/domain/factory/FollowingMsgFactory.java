package com.mirrormetech.corm.core.message.domain.factory;

import com.alibaba.fastjson.JSON;
import com.mirrormetech.corm.common.util.ZonedDateTimeUtils;
import com.mirrormetech.corm.core.chat.domain.dto.msg.SendFollowMsgDTO;
import com.mirrormetech.corm.core.message.domain.*;
import com.mirrormetech.corm.core.message.domain.message.FollowMessage;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class FollowingMsgFactory extends MessageFactory<SendFollowMsgDTO,FollowMessage> {

    @Override
    public Integer getSupportMsgType() {
        return MessageTypeEnum.FOLLOWING_MSG.getCode();
    }

    @Override
    public Message createMessage(Long sender, Long recipient, Timestamp timestamp) {
        FollowMessage followMessage = new FollowMessage();
        followMessage.setSenderId(sender);
        followMessage.setSenderType(MessageUsageType.USER.getCode());
        followMessage.setReceiverId(recipient);
        followMessage.setReceiverType(MessageUsageType.USER.getCode());
        followMessage.setTimestamp(timestamp);
        followMessage.setMessageType(MessageTypeEnum.FOLLOWING_MSG.getCode());
        followMessage.setShortContent("用户关注");
        followMessage.setMsgStatus(MessageStatus.UNREAD.getCode());
        followMessage.setMsgSource(MessageSource.APP.getCode());
        return followMessage;
    }

    @Override
    public ChatMessageDO msgToDO(FollowMessage message) {
        ChatMessageDO chatMessageDO = new ChatMessageDO();
        chatMessageDO.setSenderId(message.getSenderId());
        chatMessageDO.setSenderType(MessageUsageType.USER.getCode());
        chatMessageDO.setReceiverId(message.getReceiverId());
        chatMessageDO.setReceiverType(MessageUsageType.USER.getCode());
        chatMessageDO.setMsgType(message.getMessageType());
        chatMessageDO.setIsRead(0);
        chatMessageDO.setContent(JSON.toJSONString(message.getContent()));
        chatMessageDO.setSendTime(ZonedDateTimeUtils.getCurrentLocalDateTimeInCST());
        chatMessageDO.setPersistTime(ZonedDateTimeUtils.getCurrentLocalDateTimeInCST());
        return chatMessageDO;
    }

    @Override
    public Message DTOToMessage(SendFollowMsgDTO dto) {
        return null;
    }

    @Override
    public List<FollowMessage> DTOToMessageList(SendFollowMsgDTO dto) {
        return List.of();
    }


}
