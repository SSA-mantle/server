package com.pigeon3.ssamantle.domain.model.user.exception;

import lombok.Getter;

/**
 * User 도메인 예외
 */
@Getter
public class UserDomainException extends RuntimeException {

    private final UserDomainExceptionType exceptionType;

    private UserDomainException(UserDomainExceptionType exceptionType, String message) {
        super(message);
        this.exceptionType = exceptionType;
    }

    private UserDomainException(UserDomainExceptionType exceptionType, String message, Throwable cause) {
        super(message, cause);
        this.exceptionType = exceptionType;
    }

    /**
     * 예외 타입으로 예외 생성
     */
    public static UserDomainException of(UserDomainExceptionType type) {
        return new UserDomainException(type, type.getMessage());
    }

    /**
     * 예외 타입과 커스텀 메시지로 예외 생성
     */
    public static UserDomainException of(UserDomainExceptionType type, String customMessage) {
        return new UserDomainException(type, customMessage);
    }

    /**
     * 예외 타입과 원인으로 예외 생성
     */
    public static UserDomainException of(UserDomainExceptionType type, Throwable cause) {
        return new UserDomainException(type, type.getMessage(), cause);
    }

    /**
     * 에러 코드 반환
     */
    public String getErrorCode() {
        return exceptionType.getErrorCode();
    }
}
