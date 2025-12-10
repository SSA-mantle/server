package com.pigeon3.ssamantle.domain.model.record.exception;

/**
 * Record 도메인 예외의 기본 클래스
 */
public class RecordDomainException extends RuntimeException {
    public RecordDomainException(String message) {
        super(message);
    }

    public RecordDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
