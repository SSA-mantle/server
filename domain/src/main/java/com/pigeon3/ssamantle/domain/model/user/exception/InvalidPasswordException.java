package com.pigeon3.ssamantle.domain.model.user.exception;

/**
 * 잘못된 비밀번호 형식에 대한 예외
 */
public class InvalidPasswordException extends UserDomainException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}