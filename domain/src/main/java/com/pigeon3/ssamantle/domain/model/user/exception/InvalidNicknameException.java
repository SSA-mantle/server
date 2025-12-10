package com.pigeon3.ssamantle.domain.model.user.exception;

/**
 * 잘못된 닉네임 형식에 대한 예외
 */
public class InvalidNicknameException extends UserDomainException {
    public InvalidNicknameException(String message) {
        super(message);
    }
}