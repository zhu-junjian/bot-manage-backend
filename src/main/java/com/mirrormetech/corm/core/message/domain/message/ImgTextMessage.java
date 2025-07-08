package com.mirrormetech.corm.core.message.domain.message;

import com.alibaba.fastjson.JSON;
import com.mirrormetech.corm.core.message.domain.Message;
import com.mirrormetech.corm.core.message.domain.content.ImgTextContent;
import lombok.Data;

@Data
public class ImgTextMessage extends Message {

    private ImgTextContent content;

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
