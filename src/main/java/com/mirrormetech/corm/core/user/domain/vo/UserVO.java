package com.mirrormetech.corm.core.user.domain.vo;

import lombok.Data;

/**
 * 用户个人信息 & relation
 */
@Data
public class UserVO {

    private Boolean isOwnProfile;

    private Integer gender;

    private String phoneNum;

    private Integer followings;

    private Integer followers;

    private Integer totalLikes;

    private Long sourceUserId;

    private Long targetUserId;

    private Long id;

    private String email;

    /**
     * 用户名
     */
    private String username;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户状态 0-正常 1-禁用
     */
    private Integer status;

    /**
     * 用户唯一ID（对外展示）
     */
    private String uid;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 背景图URL
     */
    private String backgroundUrl;

    /**
     * 联名边框/头像URL
     */
    private String collaborationUrl;

    /**
     * 源用户与目标用户的关注关系
     */
    private Integer relation;
}
