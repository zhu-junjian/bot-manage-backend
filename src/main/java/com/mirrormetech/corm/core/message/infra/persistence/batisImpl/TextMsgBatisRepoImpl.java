package com.mirrormetech.corm.core.message.infra.persistence.batisImpl;

import com.mirrormetech.corm.core.chat.infra.DO.ChatSessionDO;
import com.mirrormetech.corm.core.chat.infra.persistence.ChatSessionMapper;
import com.mirrormetech.corm.core.message.domain.Message;
import com.mirrormetech.corm.core.message.domain.factory.MessageFactoryContext;
import com.mirrormetech.corm.core.message.domain.factory.TextMsgFactory;
import com.mirrormetech.corm.core.message.domain.message.TextMessage;
import com.mirrormetech.corm.core.message.domain.repository.ChatMessageRepo;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;
import com.mirrormetech.corm.core.message.infra.persistence.ChatMessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TextMsgBatisRepoImpl implements ChatMessageRepo<TextMessage> {

    private final TextMsgFactory textMsgFactory;

    private final ChatMessageMapper chatMessageMapper;

    private final ChatSessionMapper chatSessionMapper;

    private final MessageFactoryContext messageFactoryContext;

    @Override
    public ChatMessageDO persistence(TextMessage message) {
        ChatMessageDO chatMessageDO = textMsgFactory.msgToDO(message);
        String chatSessionId = Math.min(message.getSenderId(), message.getReceiverId()) +"_"
                + Math.max(message.getSenderId(), message.getReceiverId());
        ChatSessionDO chatSessionDO = chatSessionMapper.selectBySessionId(chatSessionId);
        chatMessageDO.setChatSessionId(chatSessionDO.getId());
        chatMessageMapper.insert(chatMessageDO);
        return chatMessageDO;
    }

    @Override
    public Integer getSupportedType() {
        return 3;
    }
}
