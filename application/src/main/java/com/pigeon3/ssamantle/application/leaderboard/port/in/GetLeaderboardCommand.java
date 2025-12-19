package com.pigeon3.ssamantle.application.leaderboard.port.in;

import java.time.LocalDate;

public record GetLeaderboardCommand(
    Long userId,      // 조회하는 사용자 ID (본인 순위 조회용)
    LocalDate date    // 조회할 날짜 (null이면 오늘)
) {
    public GetLeaderboardCommand {
        if (userId == null) {
            throw new IllegalArgumentException("userId는 필수입니다.");
        }
    }

    public LocalDate getEffectiveDate() {
        return date != null ? date : LocalDate.now();
    }
}
