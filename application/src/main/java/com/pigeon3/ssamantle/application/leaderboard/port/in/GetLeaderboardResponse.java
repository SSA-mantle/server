package com.pigeon3.ssamantle.application.leaderboard.port.in;

import com.pigeon3.ssamantle.domain.model.leaderboard.vo.LeaderboardEntry;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record GetLeaderboardResponse(
    LocalDate date,                     // 리더보드 날짜
    List<LeaderboardEntryDto> topRankers,   // 상위 100명
    LeaderboardEntryDto myRank          // 본인 순위 (순위권 밖이어도 포함, 없으면 null)
) {
    @Builder
    public record LeaderboardEntryDto(
        int rank,
        String nickname,
        int failCount,
        String solvedAt  // ISO 8601 format
    ) {
        public static LeaderboardEntryDto from(LeaderboardEntry entry) {
            return LeaderboardEntryDto.builder()
                .rank(entry.getRank())
                .nickname(entry.getNickname())
                .failCount(entry.getFailCount())
                .solvedAt(entry.getSolvedAt().toString())
                .build();
        }
    }

    public static GetLeaderboardResponse of(LocalDate date,
                                           List<LeaderboardEntry> topRankers,
                                           LeaderboardEntry myRank) {
        return GetLeaderboardResponse.builder()
            .date(date)
            .topRankers(topRankers.stream()
                .map(LeaderboardEntryDto::from)
                .toList())
            .myRank(myRank != null ? LeaderboardEntryDto.from(myRank) : null)
            .build();
    }
}
