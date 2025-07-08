package com.mirrormetech.corm.core.message.infra.DO;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 聊天消息表
 * chat_message
 */
@Data
@TableName("chat_message")
public class ChatMessageDO {

    // 聊天消息的主键ID
    @TableId(type = IdType.AUTO)
    private Long id;

    // 消息ID
    /*@TableField("msg_id")
    private Long msgId;*/

    // 关联chat_session.id
    @TableField("chat_session_id")
    private Long chatSessionId;

    // 消息的发送方
    @TableField("sender_id")
    private Long senderId;

    // 消息发送方类型 0-用户 1-设备
    @TableField("sender_type")
    private Integer senderType;

    // 消息的接收方
    @TableField("receiver_id")
    private Long receiverId;

    // 消息接收方类型 0-用户 1-设备
    @TableField("receiver_type")
    private Integer receiverType;

    /**
     * 消息内容的json数据
     * Content类型 的 json数据
     */
    @TableField("content")
    private String content;

    // 消息类型 对应MessageTypeEnum
    @TableField("msg_type")
    private Integer msgType;

    // 消息是否已读
    @TableField("is_read")
    private Integer isRead;

    // 消息发送时间
    @TableField("send_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime sendTime;

    // 消息入库时间
    @TableField(value = "persist_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime persistTime;

    /**
     * 设备ID
     * 接收消息的设备ID 或者以后会有 设备发送的消息？ 再考虑
     * 如果以后有设备发送的消息  可以设置一个类型为 设否设备消息
     * 如果是设备消息 还要区分设备是initiator or recipient
     */
    @TableField("device_id")
    private Long deviceId;

    // 设备接收状态 1-未接收 2-接收
    @TableField("device_receive_status")
    private  Integer deviceReceiveStatus;
}