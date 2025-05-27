package com.mirrormetech.corm.core.topic.infra.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 内容 话题
 * @author spencer
 * @date 2025/04/28
 */
@Data
@TableName("topic")
public class TopicDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 状态 0-正常 1-关闭
     */
    @TableField("status")
    private Integer status;


}
