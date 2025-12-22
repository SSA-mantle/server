package com.pigeon3.ssamantle.application.achievement.port.in;

/**
 * 사용자 업적 조회 쿼리
 *
 * @param userId 사용자 ID
 */
public record GetUserAchievementsQuery(
        Long userId
) {
}
