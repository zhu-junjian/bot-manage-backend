package com.mirrormetech.corm.common.exception;

import lombok.Getter;

@Getter
public class PhoneAlreadyRegisteredException extends RuntimeException{

    private String code;

    public PhoneAlreadyRegisteredException(String code, String message, Throwable throwable){
        super(message, throwable);
        this.code = code;
    }
}
