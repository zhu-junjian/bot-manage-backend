package com.mirrormetech.corm.core.activity.domain;

import lombok.Data;

/**
 * 活动查询对象
 */
@Data
public class ActivityQueryVO {

    private String name;

    /**
     * 关联活动所属大赛字段
     */
    private Integer type;
}
