package com.pigeon3.ssamantle.application.user.port.in;

/**
 * 내 게임 통계 조회 명령
 *
 * @param userId 사용자 ID
 */
public record GetMyGameStatisticsCommand(Long userId) {
    public GetMyGameStatisticsCommand {
        if (userId == null) {
            throw new IllegalArgumentException("userId는 필수입니다.");
        }
    }
}
