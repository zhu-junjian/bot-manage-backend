package com.mirrormetech.corm.core.message.domain.factory;

import com.alibaba.fastjson.JSON;
import com.mirrormetech.corm.common.util.ZonedDateTimeUtils;
import com.mirrormetech.corm.core.chat.domain.dto.msg.SendTitleMsgDTO;
import com.mirrormetech.corm.core.message.domain.Message;
import com.mirrormetech.corm.core.message.domain.MessageTypeEnum;
import com.mirrormetech.corm.core.message.domain.content.TitleTextContent;
import com.mirrormetech.corm.core.message.domain.message.TitleTextMessage;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class TitleTextMsgFactory extends MessageFactory<SendTitleMsgDTO, TitleTextMessage> {

    @Override
    public Integer getSupportMsgType() {
        return 4;
    }

    @Override
    public Message createMessage(Long sender, Long recipient, Timestamp timestamp) {
        return null;
    }

    @Override
    public ChatMessageDO msgToDO(TitleTextMessage message) {
        ChatMessageDO chatMessageDO = new ChatMessageDO();
        chatMessageDO.setIsRead(0);
        chatMessageDO.setSenderId(message.getSenderId());
        chatMessageDO.setSenderType(message.getSenderType());
        chatMessageDO.setReceiverId(message.getReceiverId());
        chatMessageDO.setReceiverType(message.getReceiverType());
        chatMessageDO.setContent(message.getContent());
        chatMessageDO.setMsgType(message.getMessageType());
        chatMessageDO.setSendTime(ZonedDateTimeUtils.getCurrentLocalDateTimeInCST());
        chatMessageDO.setPersistTime(ZonedDateTimeUtils.getCurrentLocalDateTimeInCST());
        return chatMessageDO;
    }

    @Override
    public Message DTOToMessage(SendTitleMsgDTO dto) {
        TitleTextMessage titleTextMessage = new TitleTextMessage();
        titleTextMessage.setMessageType(MessageTypeEnum.TITLE_TEXT.getCode());
        titleTextMessage.setMsgSource(1);
        titleTextMessage.setMsgStatus(0);
        titleTextMessage.setSenderId(dto.getSenderId());
        titleTextMessage.setReceiverId(dto.getReceiverId());
        titleTextMessage.setReceiverType(0);
        titleTextMessage.setSenderType(0);
        titleTextMessage.setTimestamp(new Timestamp(System.currentTimeMillis()));
        titleTextMessage.setContent(JSON.parseObject(dto.getContent(), TitleTextContent.class));
        return titleTextMessage;
    }

    @Override
    public List<TitleTextMessage> DTOToMessageList(SendTitleMsgDTO dto) {
        return List.of();
    }
}
