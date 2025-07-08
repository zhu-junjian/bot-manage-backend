package com.mirrormetech.corm.core.chat.domain;

import java.time.LocalDateTime;

/**
 * 会话聚合跟
 * session 实体
 * 值对象暂无
 */
public class Chat {

    /*
        1.创建会话？ 在领域服务中使用factory创建
        2.会话查询  在领域服务中调用仓储层的下水道逻辑查询
        3.会话的未读数 在消息域的领域服务中 发布一条消息将更新会话对应的未读数+1 可以是数据库 也可以是缓存
        4.置顶chat 将chat的top属性置为1 topTime为当前时间，根据topTime倒序排列
     */

    /*
        点赞和关注作为特殊的 chat处理  sessionId为空，类型为点赞和关注
     */

    /*
        缓存格式
        用户总未读数 redis.hash user:unread:total {userId} {count}
        会话未读数   redis.hash chat:unread:{session} {userId} {count}
        点赞未读数   redis.hash notify:unread:like {userId} {count}
        关注未读数   redis.hash notify:unread:follow {userId} {count}
        服务端不同消息涉对应MQTT协议的主题：
        会话消息 /corm/user/{userId}/msg
        点赞消息 /corm/user/{userId}/like
        关注消息 /corm/user/{userId}/follow
        系统通知 /corm/user/{userId}/system
        小豹官方 /corm/user/{userId}/BAOBAO
        App端关注  /corm/user/{userId}/#
     */

    //会话Id 由 min(userId1,userId2)_max(userId1,userId2)唯一组成  唯一键
    private String sessionId;

    //会话聊天的双方ID  userId1 min  userId2 max
    private Long userId1;
    private Long userId2;

    //此会话的最后一条消息的ID
    private Long laseMessageId;

    private LocalDateTime laseMessageTime;

    //会话类型 包含了 正常聊天会话 点赞会话 关注会话
    private Integer type;
}
