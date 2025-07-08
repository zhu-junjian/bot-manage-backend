package com.mirrormetech.corm.core.like.domain;

import lombok.Data;

import java.time.LocalDateTime;

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
    private LocalDateTime likeTime;

    //点赞状态 1-有效点赞 0-取消点赞
    private Integer status;

    public PostLike(){}

    // 构造方法
    public PostLike(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
        this.likeTime = LocalDateTime.now();
        this.status = 0; // 默认有效
    }

    // 取消点赞
    public void cancel() {
        this.status = 1;
    }

    // 重新点赞
    public void reactivate() {
        this.status = 0;
        this.likeTime = LocalDateTime.now();
    }

    // 检查是否有效点赞
    public boolean isLikeValid() {
        return status == 0;
    }

    // 检查是否取消点赞
    public boolean isUnLikeValid() {
        return status == 1;
    }
}
