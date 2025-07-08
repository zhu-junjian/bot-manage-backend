package com.mirrormetech.corm.core.chat.domain.dto;

import lombok.AllArgsConstructor;

/**
 * 更新场景下的条件内容
 */
@AllArgsConstructor
public class UpdateSessionCondition {

    private Integer version;

    private String sessionId;
}
