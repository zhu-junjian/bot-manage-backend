package com.mirrormetech.corm.core.message.domain;

import com.mirrormetech.corm.core.message.domain.content.Content;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 消息实体
 */
@Data
public abstract class Message {

    // 内容简介 用来在chatSession 列表中展示
    private String shortContent;

    /**
     * 发送者用户ID
     * 点赞消息：点赞者
     */
    private Long senderId;

    /**
     * 发送者类型
     * 0-用户
     * 1-设备
     */
    private Integer senderType;

    /**
     * 接收者类型
     * 0-用户
     * 1-设备
     */
    private Integer receiverType;

    /**
     * 接收者用户ID
     * 点赞消息：被点赞者
     */
    private Long receiverId;

    /**
     * 消息类型  messageTypeEnum
     */
    private Integer messageType;

    /**
     * 消息序列号
     */
    private Long msgSeqNo;

    /**
     * 消息创建 时间戳
     */
    private Timestamp timestamp;

    /**
     * 消息状态 0-未读 1-已读
     */
    private Integer msgStatus;

    /**
     * 消息源
     * 运营后端 0
     * App    1
     */
    private Integer msgSource;

    public abstract String topic();

    public abstract String getContent();

    /**
     * TODO 此部分只执行和消息域有关的逻辑  其余的逻辑应该通过领域服务跨域编排领域能力
     * 1.先更新数据库  数据库事务提交后再执行后续操作
     * 2.更新缓存数据
     * 3.发送mqtt
     * 4.发送友盟
     */
    public abstract void send();

    /**
     * 将消息推送至第三方平台 友盟 向接收方发送
     * @param message 消息实体
     */
    public abstract void publish(Message message);

    /**
     * 持久化message数据
     * //TODO 保存至数据库
     * @param message 消息实体 包含了基本参数 例如sessionId
     */
    public abstract void persistence(Message message);

    /**
     * 更新缓存中的未读数
     * 调用底层防腐层的接口 于具体实现解耦
     */
    public abstract void updateCacheCount();

}
