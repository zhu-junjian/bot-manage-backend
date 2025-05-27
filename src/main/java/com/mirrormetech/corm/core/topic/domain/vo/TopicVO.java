package com.mirrormetech.corm.core.topic.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author spencer
 * @date 2025/05/08
 */
@Data
public class TopicVO {

    private Long id;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createTime;

    /**
     * 状态 0-正常 1-关闭
     */
    private Integer status;
}
