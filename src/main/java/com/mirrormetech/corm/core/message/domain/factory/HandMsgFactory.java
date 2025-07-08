package com.mirrormetech.corm.core.message.domain.factory;

import com.mirrormetech.corm.common.util.ZonedDateTimeUtils;
import com.mirrormetech.corm.core.chat.domain.dto.msg.SendHandMsgDTO;
import com.mirrormetech.corm.core.message.domain.Message;
import com.mirrormetech.corm.core.message.domain.MessageTypeEnum;
import com.mirrormetech.corm.core.message.domain.content.HandMsg;
import com.mirrormetech.corm.core.message.domain.message.HandMessage;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class HandMsgFactory extends MessageFactory<SendHandMsgDTO, HandMessage> {
    @Override
    public Integer getSupportMsgType() {
        return MessageTypeEnum.HAND_MSG.getCode();
    }

    @Override
    public Message createMessage(Long sender, Long recipient, Timestamp timestamp) {
        return null;
    }

    @Override
    public ChatMessageDO msgToDO(HandMessage message) {
        ChatMessageDO chatMessageDO = new ChatMessageDO();
        Integer receiverType = message.getReceiverType();
        switch (receiverType) {
            case 1:
                chatMessageDO.setDeviceId(message.getReceiverId());
                chatMessageDO.setReceiverType(1);
                chatMessageDO.setReceiverId(message.getReceiverId());
                break;
            case 0:
                chatMessageDO.setReceiverId(message.getReceiverId());
                chatMessageDO.setReceiverType(0);
                chatMessageDO.setDeviceId(null);
                break;
        }
        chatMessageDO.setIsRead(0);
        chatMessageDO.setSenderId(message.getSenderId());
        chatMessageDO.setSenderType(message.getSenderType());
        chatMessageDO.setContent(message.getContent());
        chatMessageDO.setMsgType(message.getMessageType());
        chatMessageDO.setSendTime(ZonedDateTimeUtils.getCurrentLocalDateTimeInCST());
        chatMessageDO.setPersistTime(ZonedDateTimeUtils.getCurrentLocalDateTimeInCST());
        return chatMessageDO;
    }

    // 这是将多条modules合并在一条消息中的转换器
    @Override
    public Message DTOToMessage(SendHandMsgDTO dto) {
        /*HandMessage handMessage = new HandMessage();
        Integer receiverType = dto.getReceiverType();
        switch (receiverType) {
            // 0 为用户
            case 0:
                handMessage.setReceiverType(0);
                break;
            // 1 为设备
            case 1:
                handMessage.setReceiverType(1);
                break;
        }
        handMessage.setMessageType(MessageTypeEnum.HAND_MSG.getCode());
        handMessage.setReceiverId(dto.getReceiverId());
        handMessage.setMsgSource(1);
        handMessage.setMsgStatus(0);
        handMessage.setReceiverType(dto.getReceiverType());
        handMessage.setSenderId(dto.getSenderId());
        handMessage.setSenderType(0);
        handMessage.setTimestamp(new Timestamp(System.currentTimeMillis()));
        handMessage.setContent(JSON.parseObject(dto.getContent(), HandMsgContent.class));*/
        return null;
    }

    @Override
    public List<HandMessage> DTOToMessageList(SendHandMsgDTO dto) {
        List<HandMsg> modules = dto.getModules();
        List<HandMessage> messages = new ArrayList<>();
        modules.forEach(module -> {
            HandMessage handMessage = new HandMessage();
            Integer receiverType = dto.getReceiverType();
            switch (receiverType) {
                // 0 为用户
                case 0:
                    handMessage.setReceiverType(0);
                    break;
                // 1 为设备
                case 1:
                    handMessage.setReceiverType(1);
                    break;
            }
            handMessage.setMessageType(MessageTypeEnum.HAND_MSG.getCode());
            handMessage.setReceiverId(dto.getReceiverId());
            handMessage.setMsgSource(1);
            handMessage.setMsgStatus(0);
            handMessage.setReceiverType(dto.getReceiverType());
            handMessage.setSenderId(dto.getSenderId());
            handMessage.setSenderType(0);
            handMessage.setTimestamp(new Timestamp(System.currentTimeMillis()));
            handMessage.setContent(module);
            messages.add(handMessage);
        });
        return messages;
    }

}
