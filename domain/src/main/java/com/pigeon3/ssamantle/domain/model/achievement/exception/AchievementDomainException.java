package com.pigeon3.ssamantle.domain.model.achievement.exception;

import lombok.Getter;

/**
 * Achievement 도메인 예외
 */
@Getter
public class AchievementDomainException extends RuntimeException {

    private final AchievementDomainExceptionType exceptionType;

    private AchievementDomainException(AchievementDomainExceptionType exceptionType, String message) {
        super(message);
        this.exceptionType = exceptionType;
    }

    private AchievementDomainException(AchievementDomainExceptionType exceptionType, String message, Throwable cause) {
        super(message, cause);
        this.exceptionType = exceptionType;
    }

    /**
     * 예외 타입으로 예외 생성
     */
    public static AchievementDomainException of(AchievementDomainExceptionType type) {
        return new AchievementDomainException(type, type.getMessage());
    }

    /**
     * 예외 타입과 커스텀 메시지로 예외 생성
     */
    public static AchievementDomainException of(AchievementDomainExceptionType type, String customMessage) {
        return new AchievementDomainException(type, customMessage);
    }

    /**
     * 예외 타입과 원인으로 예외 생성
     */
    public static AchievementDomainException of(AchievementDomainExceptionType type, Throwable cause) {
        return new AchievementDomainException(type, type.getMessage(), cause);
    }

    /**
     * 에러 코드 반환
     */
    public String getErrorCode() {
        return exceptionType.getErrorCode();
    }
}
