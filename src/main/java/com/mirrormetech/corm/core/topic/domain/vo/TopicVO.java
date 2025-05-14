package com.mirrormetech.corm.core.topic.domain.vo;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Data
public class TopicVO {

    private Long id;

    private String name;

    private Timestamp create_time;

    /**
     * 状态 0-正常 1-关闭
     */
    private Integer status;
}
