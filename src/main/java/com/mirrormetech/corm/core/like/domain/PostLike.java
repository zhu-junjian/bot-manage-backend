package com.mirrormetech.corm.core.like.domain;

import lombok.Data;

import java.sql.Timestamp;

/**
 * 聚合根
 * @author spencer
 * @date 2025/04/28
 */
@Data
public class PostLike {

    private Long id;

    //点赞用户id
    private Long userId;

    //内容id
    private Long postId;

    //点赞时间 取消点赞时间
    private Timestamp likeTime;

    //点赞状态 1-有效点赞 0-取消点赞
    private Integer status;
}
