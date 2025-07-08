package com.mirrormetech.corm.core.message.domain.factory;

import com.mirrormetech.corm.common.util.ZonedDateTimeUtils;
import com.mirrormetech.corm.core.chat.domain.dto.msg.SendTextMsgDTO;
import com.mirrormetech.corm.core.message.domain.Message;
import com.mirrormetech.corm.core.message.domain.MessageTypeEnum;
import com.mirrormetech.corm.core.message.domain.content.TextContent;
import com.mirrormetech.corm.core.message.domain.message.TextMessage;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class TextMsgFactory extends MessageFactory<SendTextMsgDTO, TextMessage> {
    @Override
    public Integer getSupportMsgType() {
        return MessageTypeEnum.TEXT.getCode();
    }

    @Override
    public Message createMessage(Long sender, Long recipient, Timestamp timestamp) {
        return null;
    }

    /**
     * 将消息转换为数据库持久化类
     * @param message 文本消息
     * @return 数据库持久化类型
     */
    @Override
    public ChatMessageDO msgToDO(TextMessage message) {
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

    /**
     * 将数据传输对象 封装为消息
     * @param dto 前端传入的数据传输对象
     * @return 包装后的消息
     */
    @Override
    public Message DTOToMessage(SendTextMsgDTO dto) {
        TextMessage textMessage = new TextMessage();
        textMessage.setMessageType(MessageTypeEnum.TEXT.getCode());
        textMessage.setMsgSource(1);
        textMessage.setMsgStatus(0);
        textMessage.setSenderId(dto.getSenderId());
        textMessage.setReceiverId(dto.getReceiverId());
        textMessage.setReceiverType(0);
        textMessage.setSenderType(0);
        textMessage.setTimestamp(new Timestamp(System.currentTimeMillis()));
        TextContent textContent = new TextContent();
        textContent.setText(dto.getContent());
        textMessage.setContent(textContent);
        return textMessage;
    }

    @Override
    public List<TextMessage> DTOToMessageList(SendTextMsgDTO dto) {
        return List.of();
    }
}
