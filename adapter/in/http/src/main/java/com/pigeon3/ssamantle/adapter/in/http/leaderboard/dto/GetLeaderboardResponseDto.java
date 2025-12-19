package com.pigeon3.ssamantle.adapter.in.http.leaderboard.dto;

import com.pigeon3.ssamantle.application.leaderboard.port.in.GetLeaderboardResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record GetLeaderboardResponseDto(
    String date,                        // YYYY-MM-DD
    List<LeaderboardEntryDto> topRankers,
    LeaderboardEntryDto myRank
) {
    @Builder
    public record LeaderboardEntryDto(
        int rank,
        String nickname,
        int failCount,
        String solvedAt
    ) {
        public static LeaderboardEntryDto from(GetLeaderboardResponse.LeaderboardEntryDto dto) {
            return LeaderboardEntryDto.builder()
                .rank(dto.rank())
                .nickname(dto.nickname())
                .failCount(dto.failCount())
                .solvedAt(dto.solvedAt())
                .build();
        }
    }

    public static GetLeaderboardResponseDto from(GetLeaderboardResponse response) {
        return GetLeaderboardResponseDto.builder()
            .date(response.date().toString())
            .topRankers(response.topRankers().stream()
                .map(LeaderboardEntryDto::from)
                .toList())
            .myRank(response.myRank() != null ?
                LeaderboardEntryDto.from(response.myRank()) : null)
            .build();
    }
}
