package com.mirrormetech.corm.core.user.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 修改用户资料VO
 */
@Data
public class UserUpdateVO {

    private Long id;

    private String avatarUrl;

    private String backgroundUrl;

    private String nickName;

    private String bio;

    private String collaborationUrl;

    private Long uid;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime birthday;
}
