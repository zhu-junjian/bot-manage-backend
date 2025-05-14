package com.mirrormetech.corm.common.exception;

import lombok.Getter;

/**
 * 全局异常 字典类
 * 自定义来自内部的错误码和错误信息
 */
@Getter
public enum ExceptionCode {

    //登录 授权相关
    INSIDE_ERROR("494", "内部错误"),
    QUERY_ERROR_USER_NOT_EXIST("495", "查询失败，用户不存在"),
    USER_ERROR_PASSWORD("496", "登录失败，用户密码有误"),
    USER_NOT_EXIST("497", "登录失败，用户不存在"),
    MISSING_ARGUMENT("498", "参数有误"),
    PHONE_ALREADY_REGISTERED("499", "手机号码已注册"),

    //设备 相关
    DEVICE_NOT_EXISTS("D100001","设备不存在"),
    DEVICE_ARGS_NULL("D100002","设备参数为空"),

    //接口相关
    DEVICE_SN_INVALID("D200001","SN无效,设备不存在"),
    INVALID_USER_ID("D200002","无效的用户"),
    UPDATE_DEVICE_NOT_EXISTS("D200003", "更新失败，设备不存在"),
    VALIDATE_ERROR("D200004","参数校验失败"),


    //发布内容相关

    //点赞
    MISSING_ARGS("L100001","参数有误,用户信息或者内容信息不存在"),
    USER_OR_POST_NOT_EXISTS("L100002","用户或者内容不存在"),
    ALREADY_LIKED("L100003","用户已点赞"),
    ALREADY_UNLIKED("L100004", "用户已取消点赞，请勿重复取消"),

    //话题相关
    MISSING_TOPIC_NAME("T100001","参数有误,缺少话题名称"),
    DUPLICATED_TOPIC_NAME("T100002", "话题名称重复"),

    // 二级大类的一级大类不存在或者 没有传入一级大类
    MISSING_PARENT_FIRST_LEVEL("C100001","参数有误,缺少一级大类信息"),
    CATEGORY_NAME_EXISTS("C100002","大类名称已存在,请修改"),
    MISSING_OR_ERROR_CATEGORY_LEVEL("C100003","参数有误,缺少大类级别或大类级别有误"),
    MISSING_CATEGORY_NAME("C100004","参数有误,缺少大类名称"),
    FIRST_LEVEL_CATEGORY_NOT_EXISTS("C100005","一级大类不存在,请检查参数是否正确!"),
    //发布内容参数缺失
    //
    POSTS_MISSING_ARGUMENT("P100001", "发布内容失败，缺少必要参数")
    ;

    private final String exceptionCode;

    private final String exceptionMsg;

    ExceptionCode(String i, String errorMsg) {
        this.exceptionCode = i;
        this.exceptionMsg = errorMsg;
    }

}
