package com.mirrormetech.corm.core.message.domain.factory;

import com.alibaba.fastjson.JSON;
import com.mirrormetech.corm.common.util.ZonedDateTimeUtils;
import com.mirrormetech.corm.core.chat.domain.dto.msg.SendLikeMsgDTO;
import com.mirrormetech.corm.core.message.domain.*;
import com.mirrormetech.corm.core.message.domain.message.LikeMessage;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LikeMessageFactory extends MessageFactory<SendLikeMsgDTO, LikeMessage> {

    @Override
    public Integer getSupportMsgType() {
        return MessageTypeEnum.LIKE_MSG.getCode();
    }

    /**
     * 创建点赞消息
     * @param sourceUserId 点赞人
     * @param targetUserId 被赞人
     * @param timestamp 点赞时间
     * @return 点赞消息
     */
    @Override
    public Message createMessage(Long sourceUserId, Long targetUserId, Timestamp timestamp) {
        //TODO hset like userId:${userId} count  hincrby like userId:${userId} 1
        // 点赞消息content为空， 在chat_session chat_message的事务提交后 再按照接收方的ID 处理包含个人头像 等在内的基本信息
        LikeMessage likeMessage = new LikeMessage();
        likeMessage.setSenderId(sourceUserId);
        likeMessage.setSenderType(MessageUsageType.USER.getCode());
        likeMessage.setReceiverId(targetUserId);
        likeMessage.setReceiverType(MessageUsageType.USER.getCode());
        likeMessage.setTimestamp(timestamp);
        likeMessage.setMessageType(MessageTypeEnum.LIKE_MSG.getCode());
        likeMessage.setShortContent("用户点赞");
        likeMessage.setMsgStatus(MessageStatus.UNREAD.getCode());
        likeMessage.setMsgSource(MessageSource.APP.getCode());
        return likeMessage;
    }

    /**
     * 将点赞消息转换为数据库持久化对象
     * @param likeMessage 点赞消息
     * @return 持久化对象
     */
    @Override
    public ChatMessageDO msgToDO(LikeMessage likeMessage) {
        ChatMessageDO chatMessageDO = new ChatMessageDO();
        chatMessageDO.setSenderId(likeMessage.getSenderId());
        chatMessageDO.setSenderType(MessageUsageType.USER.getCode());
        chatMessageDO.setReceiverId(likeMessage.getReceiverId());
        chatMessageDO.setReceiverType(MessageUsageType.USER.getCode());
        chatMessageDO.setMsgType(likeMessage.getMessageType());
        chatMessageDO.setIsRead(0);
        chatMessageDO.setContent(JSON.toJSONString(likeMessage.getContent()));
        chatMessageDO.setSendTime(ZonedDateTimeUtils.getCurrentLocalDateTimeInCST());
        chatMessageDO.setPersistTime(ZonedDateTimeUtils.getCurrentLocalDateTimeInCST());
        return chatMessageDO;
    }

    @Override
    public Message DTOToMessage(SendLikeMsgDTO dto) {
        return null;
    }

    @Override
    public List<LikeMessage> DTOToMessageList(SendLikeMsgDTO dto) {
        return List.of();
    }
}
