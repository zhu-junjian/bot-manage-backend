package com.mirrormetech.corm.core.chat.domain.dto;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 更新场景下的待更新内容
 */
@AllArgsConstructor
public class UpdateSessionDTO {

    private Long lastMsgId;

    private String lastMsgContent;

    private Integer lastMsgType;

    private LocalDateTime lastMsgTime;

    /*// 未读数增加在哪个字段上
    private String unreadField;

    // 增加的未读数 一般场景为1 批量场景可能存在>1的情况
    private Long incrCount;*/
}
