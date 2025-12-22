package com.pigeon3.ssamantle.application.achievement.port.in;

/**
 * 업적 체크 및 부여 커맨드
 *
 * @param userId 사용자 ID
 */
public record CheckAndGrantAchievementsCommand(
        Long userId
) {
}
