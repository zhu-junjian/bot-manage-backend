package com.mirrormetech.corm.common.exception;


import lombok.Data;

@Data
public class ServiceException extends RuntimeException {

	private String code;

	public ServiceException(String code, String message, Throwable throwable) {
		super(message, throwable);
		this.code = code;
	}

	public ServiceException(String code, String message) {
		this(code, message, null);
	}

	public ServiceException(ExceptionCode exceptionCode) {
		this(exceptionCode.getExceptionCode(), exceptionCode.getExceptionMsg(), null);
	}

	public ServiceException(ExceptionCode errorMessage, Throwable throwable) {
		this(errorMessage.getExceptionCode(), errorMessage.getExceptionMsg(), throwable);
	}

	public ServiceException(ExceptionCode errorMessage, String message) {
		this(errorMessage.getExceptionCode(), message, null);
	}

	public ServiceException(ExceptionCode errorMessage, String message, Throwable throwable) {
		this(errorMessage.getExceptionCode(), message, throwable);
	}
}
