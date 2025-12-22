package com.pigeon3.ssamantle.domain.model.achievement;

/**
 * 업적 카테고리
 * - STREAK: 연속 풀이 기반 업적
 * - TOTAL_SOLVED: 총 해결 문제 수 기반 업적
 */
public enum AchievementCategory {
    /**
     * 연속 풀이 기반 업적 (User.nowCont 사용)
     */
    STREAK,

    /**
     * 총 해결 문제 수 기반 업적 (Record 통계 사용)
     */
    TOTAL_SOLVED
}
