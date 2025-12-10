package com.pigeon3.ssamantle.domain.model.problem.exception;

/**
 * Problem 도메인 예외의 기본 클래스
 */
public class ProblemDomainException extends RuntimeException {
    public ProblemDomainException(String message) {
        super(message);
    }

    public ProblemDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
