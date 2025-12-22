package com.pigeon3.ssamantle.application.achievement.port.out;

import com.pigeon3.ssamantle.domain.model.achievement.UserAchievement;

/**
 * 사용자 업적 저장 아웃바운드 포트
 */
public interface SaveUserAchievementPort {

    /**
     * 업적 저장
     *
     * @param achievement 저장할 업적
     * @return 저장된 업적
     */
    UserAchievement save(UserAchievement achievement);
}
