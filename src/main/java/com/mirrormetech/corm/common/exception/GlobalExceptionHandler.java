package com.mirrormetech.corm.common.exception;

import com.mirrormetech.corm.common.result.ApiResult;
import com.mirrormetech.corm.core.register.domain.dto.RegisterResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * @author spencer
 * @date 2025/04/18
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PhoneAlreadyRegisteredException.class)
    public ApiResult handlePhoneRegistered(PhoneAlreadyRegisteredException exception){
        log.error(exception.getMessage());
        return ApiResult.fail(ResultCode.BAD_REQUEST,
                new RegisterResponse(UserErrorMessage.USER_NAME_EXISTS.getCode(),UserErrorMessage.USER_NAME_EXISTS.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceException.class)
    public ApiResult handleServiceException(ServiceException exception){
        log.error(exception.getMessage());
        return ApiResult.fail(ResultCode.BAD_REQUEST,
                new RegisterResponse(exception.getCode(),exception.getMessage()));
    }

    /**
     * 参数校验异常处理器
     * @param exception 参数一擦汗给你
     * @return ApiResult
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult handleException(MethodArgumentNotValidException exception){
        log.error(exception.getMessage());
        String msg = exception.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
        return ApiResult.fail(ResultCode.BAD_REQUEST,
                new RegisterResponse(ExceptionCode.VALIDATE_ERROR.getExceptionCode(),msg));
    }

    @ExceptionHandler(RuntimeException.class)
    public ApiResult handleException(RuntimeException exception){
        log.error(exception.getMessage());
        return ApiResult.fail(ResultCode.INTERNAL_ERROR,
                new RegisterResponse("500",exception.getMessage()));
    }

    /*@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ApiResult handleRuntimeException(ServiceException exception){
        log.error(exception.getMessage());
        return ApiResult.fail(ResultCode.INTERNAL_ERROR,
                new RegisterResponse(exception.getCode(),exception.getMessage()));
    }*/
}