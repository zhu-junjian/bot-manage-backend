package com.mirrormetech.corm.core.chat.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatSessionDTO {

    private Long id;

    private String sessionId;

    private Long user1Id;

    private Long user2Id;

    private Long lastMsgId;

    private String lastContent;

    private Integer lastMsgType;

    private Integer unreadCount1;

    private Integer unreadCount2;

    private Integer version;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
