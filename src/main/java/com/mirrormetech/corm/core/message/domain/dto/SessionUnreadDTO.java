package com.mirrormetech.corm.core.message.domain.dto;

import lombok.Data;

@Data
public class SessionUnreadDTO {

    // 会话表主键ID
    private Long chatSessionId;

    // 未读数
    private int unread;
}
