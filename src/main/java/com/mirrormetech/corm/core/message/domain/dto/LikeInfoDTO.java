package com.mirrormetech.corm.core.message.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LikeInfoDTO {

    // 消息表主键ID
    private Long id;

    // 点赞者
    private Long senderId;

    // 被点赞者
    private Long receiverId;

    // 消息简介 "点赞了你的作品"
    private String content;

    // 消息类型 5 不需要返回前端
    //private Integer msgType;

    // 点赞时间
    private LocalDateTime sendTime;

    // 点赞者昵称
    private String nickName;

    // 点赞者头像
    private String avatarUrl;

    /**
     * 被关注者视角下 他和关注者的关注关系
     * @see com.mirrormetech.corm.core.user.domain.FollowRelation
     */
    private Integer relation;

}
