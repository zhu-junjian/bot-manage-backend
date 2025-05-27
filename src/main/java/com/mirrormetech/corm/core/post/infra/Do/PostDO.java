package com.mirrormetech.corm.core.post.infra.Do;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 数据库持久化对象
 * @author spencer
 * @date 2025/05/06
 */
@TableName("post_record")
@Getter
@Setter
public class PostDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("publish_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime publishTime;

    @TableField("location_text")
    private String locationText;

    @TableField("location")
    private String location;

    @TableField("user_id")
    private Long userId;

    @TableField("username")
    private String username;

    @TableField("nick_name")
    private String nickName;

    @TableField("avatar_url")
    private String avatarUrl;

    @TableField("content")
    private String content;

    // 封面URL
    @TableField("cover_url")
    private String coverUrl;

    @TableField("cover_content_type")
    private Integer coverContentType;

    // 封面内容数据
    @TableField("cover_content")
    private String coverContent;

    // 图片URL
    @TableField("image_url")
    private String imageUrl;

    // 音频URL
    @TableField("audio_url")
    private String audioUrl;

    // 动作URL
    @TableField("action_url")
    private String actionUrl;

    // 可见范围（枚举） 可见范围 0-public 1-private 2-friends
    @TableField("visibility")
    private Integer visibility;

    // 是否可评论
    @TableField("is_commentable")
    private Integer isCommentable;

    // 是否可分享
    @TableField("is_shareable")
    private Integer isShareable;

    // 关联活动ID
    @TableField("activity_id")
    private Long activityId;

    @TableField("first_level_category")
    private Long firstLevelCategory;

    @TableField("second_level_category")
    private Long secondLevelCategory;

    // 是否精选 0-否 1-是
    @TableField("is_featured")
    private Integer isFeatured;

    // 内容状态（枚举）
    @TableField("status")
    private Integer status;

    // 官方排序权重
    @TableField("official_weight")
    private Integer officialWeight;
}
