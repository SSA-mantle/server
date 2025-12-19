package com.pigeon3.ssamantle.application.leaderboard.port.out;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 리더보드 조회 포트
 */
public interface LoadLeaderboardPort {
    /**
     * 상위 N명의 리더보드 조회
     * @param date 조회 날짜
     * @param limit 조회할 인원 수
     * @return userId와 순위 리스트
     */
    List<LeaderboardRankDto> loadTopRankers(LocalDate date, int limit);

    /**
     * 특정 사용자의 순위 조회
     * @param date 조회 날짜
     * @param userId 사용자 ID
     * @return 순위 정보 (리더보드에 없으면 empty)
     */
    Optional<LeaderboardRankDto> loadUserRank(LocalDate date, Long userId);

    record LeaderboardRankDto(
        Long userId,
        int rank,
        int failCount,
        long solvedAtEpochMilli
    ) {}
}
