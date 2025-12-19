package com.pigeon3.ssamantle.application.leaderboard.port.out;

import java.time.LocalDate;

/**
 * 리더보드 저장 포트
 */
public interface SaveLeaderboardPort {
    /**
     * 리더보드에 사용자 기록 추가/업데이트
     * @param date 날짜
     * @param userId 사용자 ID
     * @param score 스코어 (failCount와 solvedAt 조합)
     */
    void saveOrUpdate(LocalDate date, Long userId, double score);
}
