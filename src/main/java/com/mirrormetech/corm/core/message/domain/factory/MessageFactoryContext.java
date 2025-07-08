package com.mirrormetech.corm.core.message.domain.factory;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息工厂上下文
 * 根据消息工厂不同实现支持的消息 调用不同的子类方法
 */
@Component
public class MessageFactoryContext {

    private Map<Integer, MessageFactory> factories;

    public MessageFactoryContext(List<MessageFactory> factoryList) {
        if (factories == null) {
            factories = new HashMap<>();
        }
        for (MessageFactory factory : factoryList) {
            factories.put(factory.getSupportMsgType(), factory);
        }
    }

    public MessageFactory getMessageFactory(Integer msgType) {
        return factories.get(msgType);
    }
}
