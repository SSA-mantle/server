package com.pigeon3.ssamantle.adapter.in.http.user.dto;

import com.pigeon3.ssamantle.application.user.port.in.GetMyGameStatisticsResponse;

/**
 * 내 게임 통계 조회 HTTP 응답 DTO
 */
public record GetMyGameStatisticsResponseDto(
    Long userId,
    Long totalGamesPlayed,
    Long successfulGames,
    Double winRate,
    Integer bestRank,
    Integer longestConsecutiveDays,
    Double averageAttempts
) {
    /**
     * Application Response를 HTTP DTO로 변환
     *
     * @param response 게임 통계 응답
     * @return HTTP 응답 DTO
     */
    public static GetMyGameStatisticsResponseDto from(GetMyGameStatisticsResponse response) {
        return new GetMyGameStatisticsResponseDto(
            response.userId(),
            response.totalGamesPlayed(),
            response.successfulGames(),
            response.winRate(),
            response.bestRank(),
            response.longestConsecutiveDays(),
            response.averageAttempts()
        );
    }
}
