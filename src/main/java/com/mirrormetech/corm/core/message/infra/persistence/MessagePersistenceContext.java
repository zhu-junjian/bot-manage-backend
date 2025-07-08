package com.mirrormetech.corm.core.message.infra.persistence;

import com.mirrormetech.corm.core.message.domain.Message;
import com.mirrormetech.corm.core.message.domain.repository.ChatMessageRepo;
import com.mirrormetech.corm.core.message.infra.DO.ChatMessageDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息持久化接口 策略模式上下文
 */
@Component
public class MessagePersistenceContext {

    private Map<Integer, ChatMessageRepo> strategies;

    @Autowired
    public MessagePersistenceContext(List<ChatMessageRepo> messageRepos) {
        if (strategies == null) {
            strategies = new HashMap<>();
        }
        for (ChatMessageRepo messageRepo : messageRepos) {
            boolean repoImpl = messageRepo.getClass().getName().contains("RepoImpl");
            if (repoImpl) {
                strategies.put(messageRepo.getSupportedType(), messageRepo);
            }
        }
    }

    public ChatMessageDO processMessage(Message message) {
        ChatMessageRepo strategy = strategies.get(message.getMessageType());
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported message type: " + message.getMessageType());
        }
        return strategy.persistence(message);
    }
}
