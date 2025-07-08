package com.mirrormetech.corm.core.message.domain.repository;

import com.mirrormetech.corm.core.message.domain.Message;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;
import org.springframework.stereotype.Repository;

/**
 * 聊天消息 持久化能力接口
 */
@Repository
public interface ChatMessageRepo<M extends Message> {

    /**
     * 保存消息接口
     * @param message 消息实体
     */
    ChatMessageDO persistence(M message);

    Integer getSupportedType();

}
