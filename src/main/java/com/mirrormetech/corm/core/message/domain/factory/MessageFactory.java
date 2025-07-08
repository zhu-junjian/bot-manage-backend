package com.mirrormetech.corm.core.message.domain.factory;

import com.mirrormetech.corm.core.chat.domain.dto.msg.BaseMsgDTO;
import com.mirrormetech.corm.core.message.domain.Message;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;

import java.sql.Timestamp;
import java.util.List;

public abstract class MessageFactory<T extends BaseMsgDTO, M extends Message> {

    public abstract Integer getSupportMsgType();

    public abstract Message createMessage(Long sender, Long recipient, Timestamp timestamp);

    public abstract ChatMessageDO msgToDO(M message);

    public abstract Message DTOToMessage(T dto);

    public abstract List<M> DTOToMessageList(T dto);
}
