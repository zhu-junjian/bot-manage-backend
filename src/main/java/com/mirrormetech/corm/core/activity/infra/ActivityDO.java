package com.mirrormetech.corm.core.activity.infra;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 数据库持久化对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDO {

    @TableId(type=IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("heading")
    private String heading;

    @TableField("subheading")
    private String subheading;

    @TableField("background_url")
    private String backgroundUrl;

    @TableField("official_weight")
    private Integer officialWeight;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("start_time")
    private LocalDateTime startTime;

    @TableField("end_time")
    private LocalDateTime endTime;

    @TableField("status")
    private Integer status;
}
