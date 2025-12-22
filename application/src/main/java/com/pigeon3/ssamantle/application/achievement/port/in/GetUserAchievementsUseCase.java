package com.pigeon3.ssamantle.application.achievement.port.in;

/**
 * 사용자 업적 조회 인바운드 포트 (UseCase)
 */
public interface GetUserAchievementsUseCase {

    /**
     * 사용자가 획득한 모든 업적을 조회합니다.
     *
     * @param query 조회 쿼리
     * @return 사용자 업적 목록
     */
    GetUserAchievementsResponse execute(GetUserAchievementsQuery query);
}
