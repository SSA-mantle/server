package com.pigeon3.ssamantle.domain.model.user.exception;

import lombok.Getter;

/**
 * User 도메인 예외 타입 정의
 */
@Getter
public enum UserDomainExceptionType {

    // 값 객체 유효성 검증 예외
    INVALID_EMAIL("USER_001", "올바른 이메일 형식이 아닙니다."),
    INVALID_PASSWORD("USER_002", "비밀번호 형식이 올바르지 않습니다."),
    INVALID_NICKNAME("USER_003", "닉네임 형식이 올바르지 않습니다."),

    // 도메인 규칙 위반 예외
    USER_NOT_FOUND("USER_100", "사용자를 찾을 수 없습니다."),
    INVALID_USER_STATE("USER_101", "잘못된 사용자 상태입니다."),

    // 입력 검증 예외
    INVALID_UPDATE_COMMAND("USER_200", "유저 수정 명령이 올바르지 않습니다."),
    NO_UPDATE_FIELD("USER_201", "수정할 항목이 없습니다."),

    // 비즈니스 규칙 예외
    EMAIL_DUPLICATED("USER_300", "이미 사용 중인 이메일입니다."),
    NICKNAME_DUPLICATED("USER_301", "이미 사용 중인 닉네임입니다.");

    private final String errorCode;
    private final String message;

    UserDomainExceptionType(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
