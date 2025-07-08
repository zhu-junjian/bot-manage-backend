package com.mirrormetech.corm.core.message.infra.persistence.batisImpl;

import com.mirrormetech.corm.core.chat.infra.DO.ChatSessionDO;
import com.mirrormetech.corm.core.chat.infra.persistence.ChatSessionMapper;
import com.mirrormetech.corm.core.message.domain.Message;
import com.mirrormetech.corm.core.message.domain.MessageTypeEnum;
import com.mirrormetech.corm.core.message.domain.factory.HandMsgFactory;
import com.mirrormetech.corm.core.message.domain.message.HandMessage;
import com.mirrormetech.corm.core.message.domain.repository.ChatMessageRepo;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;
import com.mirrormetech.corm.core.message.infra.persistence.ChatMessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HandMsgBatisRepoImpl implements ChatMessageRepo<HandMessage> {

    private final HandMsgFactory handMsgFactory;

    private final ChatMessageMapper chatMessageMapper;

    private final ChatSessionMapper chatSessionMapper;

    @Override
    public ChatMessageDO persistence(HandMessage message) {
        ChatMessageDO chatMessageDO = handMsgFactory.msgToDO(message);
        Integer receiverType = message.getReceiverType();
        String chatSessionId = null;
        switch (receiverType) {
            case 1:
                chatSessionId = Math.min(message.getSenderId(), message.getReceiverId()) +"+"
                        + Math.max(message.getSenderId(), message.getReceiverId());
                break;
            case 0:
                chatSessionId = Math.min(message.getSenderId(), message.getReceiverId()) +"_"
                        + Math.max(message.getSenderId(), message.getReceiverId());
        }
        ChatSessionDO chatSessionDO = chatSessionMapper.selectBySessionId(chatSessionId);
        chatMessageDO.setChatSessionId(chatSessionDO.getId());
        chatMessageMapper.insert(chatMessageDO);
        return chatMessageDO;
    }

    @Override
    public Integer getSupportedType() {
        return MessageTypeEnum.HAND_MSG.getCode();
    }
}
