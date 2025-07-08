package com.mirrormetech.corm.core.message.infra.persistence.batisImpl;

import com.mirrormetech.corm.core.message.domain.Message;
import com.mirrormetech.corm.core.message.domain.MessageTypeEnum;
import com.mirrormetech.corm.core.message.domain.factory.FollowingMsgFactory;
import com.mirrormetech.corm.core.message.domain.message.FollowMessage;
import com.mirrormetech.corm.core.message.domain.repository.ChatMessageRepo;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;
import com.mirrormetech.corm.core.message.infra.persistence.ChatMessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowMsgBatisRepoImpl implements ChatMessageRepo<FollowMessage> {

    private final ChatMessageMapper chatMessageMapper;

    private final FollowingMsgFactory followingMsgFactory;

    @Override
    public ChatMessageDO persistence(FollowMessage message) {
        ChatMessageDO chatMessageDO = followingMsgFactory.msgToDO((FollowMessage) message);
        chatMessageMapper.insert(chatMessageDO);
        return chatMessageDO;
    }

    @Override
    public Integer getSupportedType() {
        return MessageTypeEnum.FOLLOWING_MSG.getCode();
    }
}
