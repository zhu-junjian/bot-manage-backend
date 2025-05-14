package com.mirrormetech.corm.core.post.domain;

import com.mirrormetech.corm.common.util.ZonedDateTimeUtils;
import com.mirrormetech.corm.core.category.domain.FirstLevelCategory;
import com.mirrormetech.corm.core.category.domain.SecondLevelCategory;
import com.mirrormetech.corm.core.post.domain.dto.PostDTO;
import com.mirrormetech.corm.core.topic.domain.Topic;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * 内容 聚合根
 * @author spencer
 * @date 2025/05/06
 */
@Data
@NoArgsConstructor
public class Post {

    //行为 包括 1、创建 编辑图片或者手信 绑定大类  添加标签

    private Long id;

    private Timestamp publishTime;

    private String locationText;

    private String location;

    private Long userId;

    private String username;

    private String nickName;

    private String avatarUrl;

    private String content;

    // 封面URL
    private String coverUrl;

    private Integer coverContentType;

    // 封面内容数据
    private String coverContent;

    // 图片URL
    private String imageUrl;

    // 音频URL
    private String audioUrl;

    // 动作URL
    private String actionUrl;

    // 可见范围（枚举）
    private Integer visibility;

    // 是否可评论
    private Integer isCommentable;

    // 是否可分享
    private Integer isShareable;

    // 关联话题标签（值对象列表）
    private List<Topic> topics;

    //内容绑定的话题ids 发布接口使用
    private Long[] topicIds;

    // 关联活动ID
    private Long activityId;

    // 一级分类
    private FirstLevelCategory firstLevelCategoryInfo;

    private Long firstLevelCategory;

    private Long secondLevelCategory;

    // 二级分类
    private SecondLevelCategory secondLevelCategoryInfo;

    // 是否精选
    private Integer isFeatured;

    // 内容状态（枚举）
    private Integer status;

    // 官方排序权重
    private Integer officialWeight;

    private int likeCount;

    private boolean isLiked;

    /**
     * 参数校验 & 复制对象
     * @param postDTO 一次请求内容
     */
    public void createPost(PostDTO postDTO) {
        this.setLocationText(postDTO.getLocationText());
        this.publishTime = ZonedDateTimeUtils.getCurrentTimeInCST();
        this.location = postDTO.getLocation();
        this.userId = postDTO.getUserId();
        this.content = postDTO.getContent();
        this.coverUrl = postDTO.getCoverUrl();
        this.coverContentType = postDTO.getCoverContentType();
        this.coverContent = postDTO.getCoverContent();
        this.imageUrl = postDTO.getImageUrl();
        this.audioUrl = postDTO.getAudioUrl();
        this.actionUrl = postDTO.getActionUrl();
        this.visibility = postDTO.getVisibility();
        this.isCommentable = postDTO.getIsCommentable();
        this.isShareable = postDTO.getIsShareable();
        this.activityId = postDTO.getActivityId();
        this.firstLevelCategory = postDTO.getFirstLevelCategory();
        this.secondLevelCategory = postDTO.getSecondLevelCategory();
        this.isFeatured = postDTO.getIsFeatured();
        this.status = postDTO.getStatus();
        this.officialWeight = postDTO.getOfficialWeight();
        this.topicIds = postDTO.getTopics();
    }

}
