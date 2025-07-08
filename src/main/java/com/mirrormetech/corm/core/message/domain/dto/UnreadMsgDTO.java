package com.mirrormetech.corm.core.message.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class UnreadMsgDTO {

    // 所有未读数
    private int allUnread;

    // 所有未读点赞数
    private int unreadLikes;

    // 所有未读关注数
    private int unreadFollowings;

    // 所有未读消息数
    private int unreadMessages;

    // 所有未读消息>0的会话 & 未读数
    private List<SessionUnreadDTO> sessionUnread;
}
