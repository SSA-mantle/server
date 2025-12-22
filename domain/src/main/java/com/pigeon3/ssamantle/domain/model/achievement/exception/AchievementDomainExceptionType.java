package com.pigeon3.ssamantle.domain.model.achievement.exception;

import lombok.Getter;

/**
 * 업적 도메인 예외 타입
 */
@Getter
public enum AchievementDomainExceptionType {
    // 업적 유효성 검증
    INVALID_ACHIEVEMENT_TYPE("ACH_001", "유효하지 않은 업적 타입입니다."),
    INVALID_USER_ID("ACH_002", "유효하지 않은 사용자 ID입니다."),

    // 업적 상태 예외
    ACHIEVEMENT_NOT_FOUND("ACH_100", "업적을 찾을 수 없습니다."),
    ALREADY_UNLOCKED("ACH_101", "이미 획득한 업적입니다."),
    CONDITION_NOT_MET("ACH_102", "업적 조건을 만족하지 않습니다.");

    private final String errorCode;
    private final String message;

    AchievementDomainExceptionType(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
