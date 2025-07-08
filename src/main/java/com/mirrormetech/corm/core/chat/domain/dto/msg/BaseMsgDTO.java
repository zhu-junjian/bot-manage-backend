package com.mirrormetech.corm.core.chat.domain.dto.msg;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 消息发送数据传输对象基类
 */
@Data
public abstract class BaseMsgDTO implements Serializable {

    public abstract Integer getMessageType();

    public abstract String getContent();

    public abstract Integer getReceiverType();

    @Serial
    private static final long serialVersionUID = 1L;

    private Long senderId;

    private Long receiverId;
}
