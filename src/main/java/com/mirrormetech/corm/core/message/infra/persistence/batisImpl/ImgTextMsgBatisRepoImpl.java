package com.mirrormetech.corm.core.message.infra.persistence.batisImpl;

import com.mirrormetech.corm.core.chat.infra.DO.ChatSessionDO;
import com.mirrormetech.corm.core.chat.infra.persistence.ChatSessionMapper;
import com.mirrormetech.corm.core.message.domain.Message;
import com.mirrormetech.corm.core.message.domain.MessageTypeEnum;
import com.mirrormetech.corm.core.message.domain.factory.ImgTextMsgFactory;
import com.mirrormetech.corm.core.message.domain.message.ImgTextMessage;
import com.mirrormetech.corm.core.message.domain.repository.ChatMessageRepo;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;
import com.mirrormetech.corm.core.message.infra.persistence.ChatMessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImgTextMsgBatisRepoImpl implements ChatMessageRepo<ImgTextMessage> {

    private final ImgTextMsgFactory imgTextMsgFactory;

    private final ChatMessageMapper chatMessageMapper;

    private final ChatSessionMapper chatSessionMapper;

    @Override
    public ChatMessageDO persistence(ImgTextMessage message) {
        ChatMessageDO chatMessageDO = imgTextMsgFactory.msgToDO(message);
        String chatSessionId = Math.min(message.getSenderId(), message.getReceiverId()) +"_"
                + Math.max(message.getSenderId(), message.getReceiverId());
        ChatSessionDO chatSessionDO = chatSessionMapper.selectBySessionId(chatSessionId);
        chatMessageDO.setChatSessionId(chatSessionDO.getId());
        chatMessageMapper.insert(chatMessageDO);
        return chatMessageDO;
    }

    @Override
    public Integer getSupportedType() {
        return MessageTypeEnum.IMG_TEXT.getCode();
    }
}
