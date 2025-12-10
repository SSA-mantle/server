package com.pigeon3.ssamantle.domain.model.user.exception;

/**
 * 잘못된 이메일 형식에 대한 예외
 */
public class InvalidEmailException extends UserDomainException {
    public InvalidEmailException(String message) {
        super(message);
    }
}