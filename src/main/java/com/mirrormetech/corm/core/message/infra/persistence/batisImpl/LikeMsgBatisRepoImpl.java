package com.mirrormetech.corm.core.message.infra.persistence.batisImpl;

import com.mirrormetech.corm.core.message.domain.message.LikeMessage;
import com.mirrormetech.corm.core.message.domain.factory.LikeMessageFactory;
import com.mirrormetech.corm.core.message.domain.Message;
import com.mirrormetech.corm.core.message.domain.MessageTypeEnum;
import com.mirrormetech.corm.core.message.domain.repository.ChatMessageRepo;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;
import com.mirrormetech.corm.core.message.infra.persistence.ChatMessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeMsgBatisRepoImpl implements ChatMessageRepo<LikeMessage> {

    private final ChatMessageMapper chatMessageMapper;

    private final LikeMessageFactory likeMessageFactory;

    /**
     *
     * @param message 消息实体
     */
    @Override
    public ChatMessageDO persistence(LikeMessage message) {
        ChatMessageDO chatMessageDO = likeMessageFactory.msgToDO((LikeMessage) message);
        chatMessageMapper.insert(chatMessageDO);
        return chatMessageDO;
    }

    @Override
    public Integer getSupportedType() {
        return MessageTypeEnum.LIKE_MSG.getCode();
    }
}
