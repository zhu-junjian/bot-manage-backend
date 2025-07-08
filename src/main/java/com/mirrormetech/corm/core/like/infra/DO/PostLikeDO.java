package com.mirrormetech.corm.core.like.infra.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 发布内容 点赞关联表
 * @author spencer
 * @date 2025/04/28
 */
@Data
@TableName("user_post_like")
public class PostLikeDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    //点赞用户id
    @TableField("user_id")
    private Long userId;

    //内容id
    @TableField("post_id")
    private Long postId;

    //点赞时间 取消点赞时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @TableField("like_time")
    private LocalDateTime likeTime;

    //点赞状态 1-有效点赞 0-取消点赞
    @TableField("status")
    private Integer status;
}
