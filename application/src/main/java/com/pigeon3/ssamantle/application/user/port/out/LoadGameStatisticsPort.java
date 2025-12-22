package com.pigeon3.ssamantle.application.user.port.out;

/**
 * 사용자 게임 통계 조회 아웃바운드 포트
 */
public interface LoadGameStatisticsPort {
    /**
     * 사용자 ID로 게임 통계 조회
     *
     * @param userId 사용자 ID
     * @return 게임 통계 데이터
     */
    GameStatisticsData loadStatisticsByUserId(Long userId);
}
