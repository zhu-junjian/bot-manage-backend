package com.mirrormetech.corm.core.message.infra.persistence.batisImpl;

import com.mirrormetech.corm.core.chat.infra.DO.ChatSessionDO;
import com.mirrormetech.corm.core.chat.infra.persistence.ChatSessionMapper;
import com.mirrormetech.corm.core.message.domain.factory.TitleTextMsgFactory;
import com.mirrormetech.corm.core.message.domain.message.TitleTextMessage;
import com.mirrormetech.corm.core.message.domain.repository.ChatMessageRepo;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;
import com.mirrormetech.corm.core.message.infra.persistence.ChatMessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TitleTextMsgBatisRepoImpl implements ChatMessageRepo<TitleTextMessage> {

    private final TitleTextMsgFactory titleTextMsgFactory;

    private final ChatMessageMapper chatMessageMapper;

    private final ChatSessionMapper chatSessionMapper;

    @Override
    public ChatMessageDO persistence(TitleTextMessage message) {
        ChatMessageDO chatMessageDO = titleTextMsgFactory.msgToDO(message);
        String chatSessionId = Math.min(message.getSenderId(), message.getReceiverId()) +"_"
                + Math.max(message.getSenderId(), message.getReceiverId());
        ChatSessionDO chatSessionDO = chatSessionMapper.selectBySessionId(chatSessionId);
        chatMessageDO.setChatSessionId(chatSessionDO.getId());
        chatMessageMapper.insert(chatMessageDO);
        return chatMessageDO;
    }

    @Override
    public Integer getSupportedType() {
        return 4;
    }
}
