package com.pigeon3.ssamantle.application.achievement.port.out;

import com.pigeon3.ssamantle.domain.model.achievement.UserAchievement;

import java.util.List;

/**
 * 사용자 업적 조회 아웃바운드 포트
 */
public interface LoadUserAchievementsPort {

    /**
     * 사용자 ID로 모든 업적 조회
     *
     * @param userId 사용자 ID
     * @return 사용자가 획득한 업적 목록
     */
    List<UserAchievement> loadByUserId(Long userId);
}
