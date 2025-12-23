package com.pigeon3.ssamantle.application.common.exception;

import lombok.Getter;

/**
 * 예외 타입 정의
 */
@Getter
public enum ExceptionType {

    // 공통 예외
    BAD_REQUEST("C400", 400, "잘못된 요청입니다."),
    UNAUTHORIZED("C401", 401, "인증이 필요합니다."),
    FORBIDDEN("C403", 403, "접근 권한이 없습니다."),
    NOT_FOUND("C404", 404, "리소스를 찾을 수 없습니다."),
    CONFLICT("C409", 409, "리소스 충돌이 발생했습니다."),
    VALIDATION_ERROR("C422", 422, "유효성 검증에 실패했습니다."),
    INTERNAL_SERVER_ERROR("S500", 500, "서버 내부 오류가 발생했습니다."),

    // 사용자 예외
    USER_NOT_FOUND("U001", 404, "사용자를 찾을 수 없습니다."),
    INVALID_EMAIL("U002", 400, "올바른 이메일 형식이 아닙니다."),
    INVALID_PASSWORD("U003", 400, "비밀번호 형식이 올바르지 않습니다."),
    INVALID_NICKNAME("U004", 400, "닉네임 형식이 올바르지 않습니다."),
    DUPLICATE_EMAIL("U005", 409, "이미 존재하는 이메일입니다."),
    DUPLICATE_NICKNAME("U006", 409, "이미 존재하는 닉네임입니다."),
    USER_DELETED("U007", 400, "탈퇴한 사용자입니다."),
    NO_UPDATE_FIELD("U008", 400, "수정할 항목이 없습니다."),

    // 인증 예외
    INVALID_CREDENTIALS("A001", 401, "이메일 또는 비밀번호가 일치하지 않습니다."),
    INVALID_TOKEN("A002", 401, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("A003", 401, "만료된 토큰입니다."),

    // 게임 예외
    PROBLEM_NOT_FOUND("G001", 404, "오늘의 문제를 찾을 수 없습니다."),
    RECORD_NOT_FOUND("G002", 404, "게임 기록을 찾을 수 없습니다."),
    ALREADY_SOLVED("G003", 409, "이미 해결된 문제입니다."),
    ALREADY_GAVE_UP("G004", 409, "이미 포기한 문제입니다."),
    NO_GAME_IN_PROGRESS("G005", 404, "진행 중인 게임이 없습니다."),
    SIMILARITY_CALCULATION_FAILED("G006", 500, "유사도 계산에 실패했습니다."),
    WORD_NOT_FOUND("G007", 400, "없는 단어입니다.");

    private final String errorCode;
    private final int httpStatusCode;
    private final String message;

    ExceptionType(String errorCode, int httpStatusCode, String message) {
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
        this.message = message;
    }

}