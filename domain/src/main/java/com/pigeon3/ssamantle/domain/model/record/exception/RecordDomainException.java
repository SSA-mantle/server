package com.pigeon3.ssamantle.domain.model.record.exception;

import lombok.Getter;

/**
 * Record 도메인 예외의 기본 클래스
 */
@Getter
public class RecordDomainException extends RuntimeException {

    private final RecordDomainExceptionType exceptionType;

    private RecordDomainException(RecordDomainExceptionType type, String message) {
        super(message);
        this.exceptionType = type;
    }

    private RecordDomainException(RecordDomainExceptionType type, String message, Throwable cause) {
        super(message, cause);
        this.exceptionType = type;
    }

    public static RecordDomainException of(RecordDomainExceptionType type) {
        return new RecordDomainException(type, type.getMessage());
    }

    public static RecordDomainException of(RecordDomainExceptionType type, String customMessage) {
        return new RecordDomainException(type, customMessage);
    }

    public static RecordDomainException of(RecordDomainExceptionType type, Throwable cause) {
        return new RecordDomainException(type, type.getMessage(), cause);
    }

    public String getErrorCode() {
        return exceptionType.getErrorCode();
    }
}
