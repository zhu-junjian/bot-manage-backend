package com.mirrormetech.corm.core.post.domain.dto;

import lombok.Data;

@Data
public class PostDTO {

    /**
     * 发布时间 (ISO 8601格式)
     */
    /*@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss ", timezone = "Asia/Shanghai")
    private Timestamp publishTime;*/

    /**
     * 地址描述文本
     */
    private String locationText;

    /**
     * 地理坐标对象
     */
    private String location;

    /**
     * 用户ID (选填，默认当前登录用户)
     */
    private Long userId;

    /**
     * 内容描述
     */
    private String content;

    /**
     * 内容类别
     * 0-图片 1-gif 2-视频 3-像素图
     */
    private Integer coverContentType;

    /**
     * 封面URL
     */
    private String coverUrl;

    /**
     * 封面内容数据
     */
    private String coverContent;

    /**
     * 图片URL
     */
    private String imageUrl;

    /**
     * 音频URL
     */
    private String audioUrl;

    /**
     * 动作视频URL
     */
    private String actionUrl;

    /**
     * 可见范围  0-public 1-private 2-friends
     */
    private Integer visibility;

    /**
     * 是否可评论 (0-否 1-是，默认1)
     */
    private Integer isCommentable;

    /**
     * 是否可引用 (0-否 1-是，默认1)
     */
    private Integer isShareable;

    /**
     * 话题标签数组
     */
    private Long[] topics;

    /**
     * 所属活动ID (选填)
     */
    private Long activityId;

    /**
     * 一级分类ID
     */
    private Long firstLevelCategory;

    /**
     * 二级分类ID
     */
    private Long secondLevelCategory;

    /**
     * 是否精选 (0-否 1-是，默认0)
     */
    private Integer isFeatured;

    /**
     * 内容状态 0-normal, 1-blocked, 2-removed,3-officialRemoved,4-officialBlocked,5-private
     */
    private Integer status;

    /**
     * 官方排序权重 (选填)
     */
    private Integer officialWeight;

}
