package com.pigeon3.ssamantle.application.achievement.port.in;

import com.pigeon3.ssamantle.domain.model.achievement.AchievementType;

import java.util.List;

/**
 * 업적 체크 및 부여 인바운드 포트 (UseCase)
 */
public interface CheckAndGrantAchievementsUseCase {

    /**
     * 업적 조건을 평가하고 새로 획득한 업적을 부여합니다.
     *
     * @param command 업적 체크 커맨드
     * @return 새로 획득한 업적 타입 목록
     */
    List<AchievementType> execute(CheckAndGrantAchievementsCommand command);
}
