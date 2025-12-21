package com.pigeon3.ssamantle.application.leaderboard.port.out;

import java.time.LocalDate;

/**
 * 특정 일자의 리더보드를 초기화(삭제)하는 Port.
 *
 * - Redis ZSET key(leaderboard:{date})를 삭제하는 방식으로 구현하는 것을 전제로 한다.
 */
public interface ClearLeaderboardPort {

    /**
     * 해당 날짜의 리더보드를 초기화한다.
     */
    void clear(LocalDate date);
}
