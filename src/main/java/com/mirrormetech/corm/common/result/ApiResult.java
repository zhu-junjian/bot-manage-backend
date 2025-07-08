package com.mirrormetech.corm.common.result;

import com.mirrormetech.corm.common.exception.ResultCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果封装
 * @param <T> 接口内部相应内容
 */
@Data
public class ApiResult<T> implements Serializable {
    private String status;     // 状态码
    private String message;  // 提示信息
    private T data;          // 返回数据
    private Long timestamp; // 时间戳

    // 构造函数私有化
    private ApiResult() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> ApiResult<T> success() {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.setStatus(ResultCode.SUCCESS.getStatus());
        apiResult.setMessage(ResultCode.SUCCESS.getMsg());
        return apiResult;
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> ApiResult<T> success(T data) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.setStatus(ResultCode.SUCCESS.getStatus());
        apiResult.setMessage(ResultCode.SUCCESS.getMsg());
        apiResult.setData(data);
        return apiResult;
    }

    public static <T> ApiResult<T> success(T data, String status, String message) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.setStatus(status);
        apiResult.setMessage(message);
        apiResult.setData(data);
        return apiResult;
    }

    /**
     * 失败响应
     * @param resultCode 状态码枚举
     */
    public static <T> ApiResult<T> fail(ResultCode resultCode) {
        return fail(resultCode.getStatus(), resultCode.getMsg());
    }

    /**
     * 失败响应
     * @param resultCode 状态码枚举
     */
    public static <T> ApiResult<T> fail(ResultCode resultCode, T data) {
        return fail(resultCode.getStatus(), resultCode.getMsg(), data);
    }


    /**
     * 失败响应（自定义消息）
     */
    public static <T> ApiResult<T> fail(String status, String message) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.setStatus(status);
        apiResult.setMessage(message);
        return apiResult;
    }

    /**
     * 失败响应（自定义消息）
     */
    public static <T> ApiResult<T> fail(String status, String message, T data) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.setStatus(status);
        apiResult.setMessage(message);
        apiResult.setData(data);
        return apiResult;
    }

    // 链式调用方法
    public ApiResult<T> code(String status) {
        this.status = status;
        return this;
    }

    public ApiResult<T> message(String message) {
        this.message = message;
        return this;
    }

    public ApiResult<T> data(T data) {
        this.data = data;
        return this;
    }
}
