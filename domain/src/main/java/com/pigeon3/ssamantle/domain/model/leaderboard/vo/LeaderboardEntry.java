package com.pigeon3.ssamantle.domain.model.leaderboard.vo;

import java.time.LocalDateTime;
import lombok.Getter;

/**
 * 리더보드 항목 값 객체
 */
@Getter
public class LeaderboardEntry {
    private final int rank;           // 순위
    private final Long userId;         // 사용자 ID
    private final String nickname;     // 닉네임
    private final int failCount;       // 시도 횟수
    private final LocalDateTime solvedAt;  // 해결 시간

    private LeaderboardEntry(int rank, Long userId, String nickname,
                            int failCount, LocalDateTime solvedAt) {
        this.rank = rank;
        this.userId = userId;
        this.nickname = nickname;
        this.failCount = failCount;
        this.solvedAt = solvedAt;
    }

    public static LeaderboardEntry of(int rank, Long userId, String nickname,
                                      int failCount, LocalDateTime solvedAt) {
        validateRank(rank);
        validateUserId(userId);
        validateNickname(nickname);
        validateFailCount(failCount);
        validateSolvedAt(solvedAt);

        return new LeaderboardEntry(rank, userId, nickname, failCount, solvedAt);
    }

    private static void validateRank(int rank) {
        if (rank < 1) {
            throw new IllegalArgumentException("순위는 1 이상이어야 합니다: " + rank);
        }
    }

    private static void validateUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID는 필수입니다.");
        }
    }

    private static void validateNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("닉네임은 필수입니다.");
        }
    }

    private static void validateFailCount(int failCount) {
        if (failCount < 0) {
            throw new IllegalArgumentException("시도 횟수는 0 이상이어야 합니다: " + failCount);
        }
    }

    private static void validateSolvedAt(LocalDateTime solvedAt) {
        if (solvedAt == null) {
            throw new IllegalArgumentException("해결 시간은 필수입니다.");
        }
    }
}
