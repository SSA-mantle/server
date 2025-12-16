package com.pigeon3.ssamantle.domain.model.record.exception;

import lombok.Getter;

@Getter
public enum RecordDomainExceptionType {

    // 도메인 규칙 위반 예외
    ALREADY_SOLVED("RECORD_100", "이미 해결된 문제입니다."),
    ALREADY_GAVE_UP("RECORD_101", "이미 포기한 문제입니다."),
    RECORD_NOT_FOUND("RECORD_102", "기록을 찾을 수 없습니다."),
    INVALID_RECORD_STATE("RECORD_103", "잘못된 기록 상태입니다.");

    private final String errorCode;
    private final String message;

    RecordDomainExceptionType(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
