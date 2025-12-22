package com.pigeon3.ssamantle.application.user.port.in;

import com.pigeon3.ssamantle.application.user.port.out.GameStatisticsData;
import com.pigeon3.ssamantle.domain.model.user.User;

/**
 * 내 게임 통계 조회 응답
 *
 * @param userId 사용자 ID
 * @param totalGamesPlayed 총 게임 수
 * @param successfulGames 성공한 게임 수
 * @param winRate 승률 (%)
 * @param bestRank 최고 순위
 * @param longestConsecutiveDays 최장 연속 기록 (일)
 * @param averageAttempts 평균 시도 횟수
 */
public record GetMyGameStatisticsResponse(
    Long userId,
    Long totalGamesPlayed,
    Long successfulGames,
    Double winRate,
    Integer bestRank,
    Integer longestConsecutiveDays,
    Double averageAttempts
) {
    /**
     * User와 GameStatisticsData로부터 응답 생성
     *
     * @param user 사용자 도메인 모델
     * @param statisticsData 게임 통계 데이터
     * @return 게임 통계 응답
     */
    public static GetMyGameStatisticsResponse of(User user, GameStatisticsData statisticsData) {
        return new GetMyGameStatisticsResponse(
            user.getId(),
            statisticsData.totalGamesPlayed(),
            statisticsData.successfulGames(),
            calculateWinRate(statisticsData),
            user.getBestRank(),
            user.getLongestCont(),
            statisticsData.averageAttempts()
        );
    }

    /**
     * 승률 계산
     *
     * @param statisticsData 게임 통계 데이터
     * @return 승률 (%)
     */
    private static Double calculateWinRate(GameStatisticsData statisticsData) {
        if (statisticsData.totalGamesPlayed() == 0) {
            return 0.0;
        }
        return (statisticsData.successfulGames().doubleValue() / statisticsData.totalGamesPlayed()) * 100;
    }
}
