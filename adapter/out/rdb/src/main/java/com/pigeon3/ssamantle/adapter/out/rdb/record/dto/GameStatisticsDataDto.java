package com.pigeon3.ssamantle.adapter.out.rdb.record.dto;

import com.pigeon3.ssamantle.application.user.port.out.GameStatisticsData;

/**
 * 게임 통계 데이터 DTO (MyBatis 결과 매핑용)
 */
public record GameStatisticsDataDto(
    Long totalGamesPlayed,
    Long successfulGames,
    Double averageAttempts
) {
    /**
     * DTO를 도메인 객체로 변환
     *
     * @return 게임 통계 데이터
     */
    public GameStatisticsData toDomain() {
        return new GameStatisticsData(
            totalGamesPlayed != null ? totalGamesPlayed : 0L,
            successfulGames != null ? successfulGames : 0L,
            averageAttempts != null ? averageAttempts : 0.0
        );
    }
}
