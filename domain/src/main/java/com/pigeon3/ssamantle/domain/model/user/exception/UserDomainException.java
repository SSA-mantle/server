package com.pigeon3.ssamantle.domain.model.user.exception;

/**
 * User 도메인 예외의 기본 클래스
 */
public class UserDomainException extends RuntimeException {
    public UserDomainException(String message) {
        super(message);
    }

    public UserDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
