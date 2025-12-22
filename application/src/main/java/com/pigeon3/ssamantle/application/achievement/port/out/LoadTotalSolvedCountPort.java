package com.pigeon3.ssamantle.application.achievement.port.out;

/**
 * 사용자의 총 해결 문제 수 조회 아웃바운드 포트
 */
public interface LoadTotalSolvedCountPort {

    /**
     * 사용자의 총 해결 문제 수 조회
     *
     * @param userId 사용자 ID
     * @return 총 해결한 문제 수
     */
    int loadTotalSolvedCount(Long userId);
}
