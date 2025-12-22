package com.pigeon3.ssamantle.application.user.port.in;

/**
 * 내 게임 통계 조회 인바운드 포트
 */
public interface GetMyGameStatisticsUseCase {
    /**
     * 내 게임 통계 조회
     *
     * @param command 조회 명령
     * @return 게임 통계 응답
     */
    GetMyGameStatisticsResponse execute(GetMyGameStatisticsCommand command);
}