package com.mirrormetech.corm.core.topic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 内容 话题
 * @author spencer
 * @date 2025/04/28
 */
@Data
public class Topic {

    private Long id;

    private String name;

    private Timestamp create_time;

    /**
     * 状态 0-正常 1-关闭
     */
    private Integer status;


}
