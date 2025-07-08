package com.mirrormetech.corm.core.message.domain.message;

import com.alibaba.fastjson.JSON;
import com.mirrormetech.corm.core.message.domain.Message;
import com.mirrormetech.corm.core.message.domain.content.TitleTextContent;
import lombok.Data;

/**
 * 标题文本消息
 */
@Data
public class TitleTextMessage extends Message {

   private TitleTextContent content;

    @Override
    public String topic() {
        return "/corm/user/"+getReceiverId()+"/msg";
    }

    @Override
    public String getContent() {
        return JSON.toJSONString(content);
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
