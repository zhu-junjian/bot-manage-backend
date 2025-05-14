package com.mirrormetech.corm.common.exception;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS("success", "请求成功"),
    BAD_REQUEST("error", "请求参数错误"),
    UNAUTHORIZED("error", "未授权访问"),
    FORBIDDEN("error", "禁止访问"),
    NOT_FOUND("error", "资源不存在"),
    INTERNAL_ERROR("error", "服务器内部错误"),
    PHONE_ALREADY_REGISTERED("error", "手机已注册");

    private String status;
    private String msg;

    ResultCode(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
