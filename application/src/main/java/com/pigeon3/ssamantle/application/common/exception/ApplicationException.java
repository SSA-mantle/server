package com.pigeon3.ssamantle.application.common.exception;

import lombok.Getter;

/**
 * Application 계층 예외 기본 클래스
 */
@Getter
public class ApplicationException extends RuntimeException {

    private final String errorCode;
    private final int httpStatusCode;

    public ApplicationException(String errorCode, int httpStatusCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
    }

	/**
     * ExceptionType으로 예외 생성
     */
    public static ApplicationException of(ExceptionType type) {
        return new ApplicationException(
            type.getErrorCode(),
            type.getHttpStatusCode(),
            type.getMessage()
        );
    }

    /**
     * ExceptionType과 커스텀 메시지로 예외 생성
     */
    public static ApplicationException of(ExceptionType type, String message) {
        return new ApplicationException(
            type.getErrorCode(),
            type.getHttpStatusCode(),
            message
        );
    }
}