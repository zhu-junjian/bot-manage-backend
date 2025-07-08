package com.mirrormetech.corm.core.like.domain.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.sql.Timestamp;

@Getter
public class LikeDomainEvent extends ApplicationEvent {

    /**
     * 点赞用户
     */
    private final Long sourceId;

    /**
     * 被点赞用户
     */
    private final Long targetId;

    /**
     * 点赞事件
     */
    private final Timestamp likedTime;

    public LikeDomainEvent(Object source, Long sourceId, Long targetId, Timestamp likedTime) {
        super(source);
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.likedTime = likedTime;
    }

}
