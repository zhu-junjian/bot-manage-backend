package com.mirrormetech.corm.core.like.domain.event;

import com.mirrormetech.corm.common.server.MqttMsgBack;
import com.mirrormetech.corm.common.server.MqttPubClient;
import com.mirrormetech.corm.core.message.domain.factory.LikeMessageFactory;
import com.mirrormetech.corm.core.message.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.sql.Timestamp;

@Component
@RequiredArgsConstructor
public class LikeEventListener {

    private final MqttMsgBack mqttMsgBack;

    private final MqttPubClient mqttPubClient;

    @EventListener
    //@Async("likeEventTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleLikeCreatedEvent(LikeDomainEvent event) {
        // 1. 构建点赞消息
        System.out.printf("like event: %s\n", event);
        Long sourceId = event.getSourceId();
        Long targetId = event.getTargetId();
        Timestamp likedTime = event.getLikedTime();
        LikeMessageFactory factory = new LikeMessageFactory();
        Message message = factory.createMessage(sourceId, targetId, likedTime);
        message.send();
    }

}
