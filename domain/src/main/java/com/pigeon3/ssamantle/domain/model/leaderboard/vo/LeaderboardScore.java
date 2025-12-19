package com.pigeon3.ssamantle.domain.model.leaderboard.vo;

import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.Getter;

/**
 * 리더보드 스코어 계산 값 객체
 * failCount와 solvedAt를 조합하여 Redis Sorted Set의 score로 사용
 */
@Getter
public class LeaderboardScore {
    private static final long FAIL_COUNT_MULTIPLIER = 10_000_000_000_000L; // 10^13

    private final int failCount;
    private final LocalDateTime solvedAt;
    private final double score;

    private LeaderboardScore(int failCount, LocalDateTime solvedAt) {
        this.failCount = failCount;
        this.solvedAt = solvedAt;
        this.score = calculateScore(failCount, solvedAt);
    }

    public static LeaderboardScore of(int failCount, LocalDateTime solvedAt) {
        validateFailCount(failCount);
        validateSolvedAt(solvedAt);
        return new LeaderboardScore(failCount, solvedAt);
    }

    private static void validateFailCount(int failCount) {
        if (failCount < 0) {
            throw new IllegalArgumentException("실패 횟수는 0 이상이어야 합니다: " + failCount);
        }
    }

    private static void validateSolvedAt(LocalDateTime solvedAt) {
        if (solvedAt == null) {
            throw new IllegalArgumentException("해결 시간은 필수입니다.");
        }
    }

    private double calculateScore(int failCount, LocalDateTime solvedAt) {
        long epochMilli = solvedAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return (double) (failCount * FAIL_COUNT_MULTIPLIER) + epochMilli;
    }
}
