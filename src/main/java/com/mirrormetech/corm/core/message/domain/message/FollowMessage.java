package com.mirrormetech.corm.core.message.domain.message;

import com.mirrormetech.corm.core.message.domain.Message;
import com.mirrormetech.corm.core.message.domain.content.FollowingContent;

/**
 * 关注类型消息
 */
public class FollowMessage extends Message {

    private FollowingContent content;

    @Override
    public String topic() {
        return "/corm/user/"+getReceiverId()+"/follow";
    }

    @Override
    public String getContent() {
        return "";
    }

    @Override
    public void send() {

    }

    @Override
    public void publish(Message message) {

    }

    @Override
    public void persistence(Message message) {

    }

    @Override
    public void updateCacheCount() {

    }
}
