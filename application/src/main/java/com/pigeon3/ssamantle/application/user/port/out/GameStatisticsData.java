package com.pigeon3.ssamantle.application.user.port.out;

/**
 * 게임 통계 데이터
 *
 * @param totalGamesPlayed 총 게임 수 (모든 기록 포함)
 * @param successfulGames 성공한 게임 수 (solved_at이 있는 경우)
 * @param averageAttempts 평균 시도 횟수 (성공한 게임의 fail_count 평균)
 */
public record GameStatisticsData(
    Long totalGamesPlayed,
    Long successfulGames,
    Double averageAttempts
) {
}
