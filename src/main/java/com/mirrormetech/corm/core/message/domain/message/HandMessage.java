package com.mirrormetech.corm.core.message.domain.message;

import com.alibaba.fastjson.JSON;
import com.mirrormetech.corm.core.message.domain.Message;
import com.mirrormetech.corm.core.message.domain.content.HandMsg;
import lombok.Data;

/**
 * 手信消息
 */
@Data
public class HandMessage extends Message {

    //private HandMsgContent content;
    // 将手信信息从列表中解析
    private HandMsg content;

    /**
     * 设备接收状态
     * 1-未接受
     * 2-接收，为播放
     * 3-接收，已播放
     */
    private Integer receiverStatus;

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
