package com.mirrormetech.corm.core.user.domain;

import lombok.Getter;

/**
 * 用户间相互关注关系
 */
@Getter
public enum FollowRelation {

    SELF(-1, "用户自身"),
    NO_RELATION(0, "双方无关注关系"),    // 互不关注
    FOLLOWING(1, "当前用户已关注目标用户"),      // 已关注
    FOLLOWED_BY(2, "目标用户已关注当前用户（未回关）"),    // 回关
    MUTUAL(3, "双方互相关注");          // 已互关

    private final Integer code;

    private final String desc;

    FollowRelation(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
