package com.mirrormetech.corm.core.message.domain.factory;

import com.alibaba.fastjson.JSON;
import com.mirrormetech.corm.common.util.ZonedDateTimeUtils;
import com.mirrormetech.corm.core.chat.domain.dto.msg.SendImgMsgDTO;
import com.mirrormetech.corm.core.message.domain.Message;
import com.mirrormetech.corm.core.message.domain.MessageTypeEnum;
import com.mirrormetech.corm.core.message.domain.content.ImgTextContent;
import com.mirrormetech.corm.core.message.domain.message.ImgTextMessage;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class ImgTextMsgFactory extends MessageFactory<SendImgMsgDTO, ImgTextMessage> {
    @Override
    public Integer getSupportMsgType() {
        return MessageTypeEnum.IMG_TEXT.getCode();
    }

    @Override
    public Message createMessage(Long sender, Long recipient, Timestamp timestamp) {
        return null;
    }

    @Override
    public ChatMessageDO msgToDO(ImgTextMessage message) {
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
    public Message DTOToMessage(SendImgMsgDTO dto) {
        ImgTextMessage imgTextMessage = new ImgTextMessage();
        imgTextMessage.setMessageType(MessageTypeEnum.IMG_TEXT.getCode());
        imgTextMessage.setMsgSource(1);
        imgTextMessage.setMsgStatus(0);
        imgTextMessage.setSenderId(dto.getSenderId());
        imgTextMessage.setReceiverId(dto.getReceiverId());
        imgTextMessage.setReceiverType(0);
        imgTextMessage.setSenderType(0);
        imgTextMessage.setTimestamp(new Timestamp(System.currentTimeMillis()));
        imgTextMessage.setContent(JSON.parseObject(dto.getContent(), ImgTextContent.class));
        return imgTextMessage;
    }

    @Override
    public List<ImgTextMessage> DTOToMessageList(SendImgMsgDTO dto) {
        return List.of();
    }

}
