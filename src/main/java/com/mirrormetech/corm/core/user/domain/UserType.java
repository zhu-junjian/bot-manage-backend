package com.mirrormetech.corm.core.user.domain;

import lombok.Getter;

@Getter
public enum UserType {

    ADMINISTRATION(9, "管理员用户"),
    NORMAL(0, "普通用户");

    private Integer code;

    private String desc;

    UserType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
