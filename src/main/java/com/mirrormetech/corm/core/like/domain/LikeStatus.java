package com.mirrormetech.corm.core.like.domain;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public enum LikeStatus {

    LIKE(0, "点赞"),
    UNLIKE(1, "取消点赞");

    private static final Set<Integer> names = new HashSet<>();

    static {
        for (LikeStatus status : values()) {
            names.add(status.code);
        }
    }

    public static boolean contains(Integer value) {
        return names.contains(value);
    }

    private LikeStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    private Integer code;

    private String desc;
}
