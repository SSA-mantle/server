package com.pigeon3.ssamantle.domain.model.achievement;

import lombok.Getter;

/**
 * 업적 타입
 * 모든 업적의 정의를 포함
 */
@Getter
public enum AchievementType {
    // 연속 풀이 업적
    STREAK_3_DAYS("3일 연속 풀이", "3일 연속으로 문제를 해결했습니다", AchievementCategory.STREAK, 3),
    STREAK_7_DAYS("일주일 연속 풀이", "7일 연속으로 문제를 해결했습니다", AchievementCategory.STREAK, 7),
    STREAK_30_DAYS("한 달 연속 풀이", "30일 연속으로 문제를 해결했습니다", AchievementCategory.STREAK, 30),

    // 총 해결 문제 수 업적
    TOTAL_10_SOLVED("문제 10개 해결", "총 10개의 문제를 해결했습니다", AchievementCategory.TOTAL_SOLVED, 10),
    TOTAL_50_SOLVED("문제 50개 해결", "총 50개의 문제를 해결했습니다", AchievementCategory.TOTAL_SOLVED, 50),
    TOTAL_100_SOLVED("문제 100개 해결", "총 100개의 문제를 해결했습니다", AchievementCategory.TOTAL_SOLVED, 100);

    private final String title;
    private final String description;
    private final AchievementCategory category;
    private final int threshold;

    AchievementType(String title, String description, AchievementCategory category, int threshold) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.threshold = threshold;
    }

    /**
     * 업적 달성 조건을 충족하는지 확인
     *
     * @param streakCount 현재 연속 풀이 일수
     * @param totalSolved 총 해결한 문제 수
     * @return 조건 충족 여부
     */
    public boolean isUnlocked(int streakCount, int totalSolved) {
        return switch (category) {
            case STREAK -> streakCount >= threshold;
            case TOTAL_SOLVED -> totalSolved >= threshold;
        };
    }

    /**
     * 새로 획득할 업적 목록을 평가합니다.
     *
     * @param currentStreak        현재 연속 풀이 일수
     * @param totalSolved          총 해결한 문제 수
     * @param existingAchievements 이미 획득한 업적 타입 목록
     * @return 새로 획득할 업적 타입 목록
     */
    public static java.util.List<AchievementType> evaluateNewAchievements(
            int currentStreak,
            int totalSolved,
            java.util.List<AchievementType> existingAchievements) {
        return java.util.Arrays.stream(values())
                .filter(type -> !existingAchievements.contains(type))
                .filter(type -> type.isUnlocked(currentStreak, totalSolved))
                .toList();
    }
}
