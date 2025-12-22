package com.pigeon3.ssamantle.application.achievement.service;

import com.pigeon3.ssamantle.application.achievement.port.in.CheckAndGrantAchievementsCommand;
import com.pigeon3.ssamantle.application.achievement.port.in.CheckAndGrantAchievementsUseCase;
import com.pigeon3.ssamantle.application.achievement.port.out.LoadTotalSolvedCountPort;
import com.pigeon3.ssamantle.application.achievement.port.out.LoadUserAchievementsPort;
import com.pigeon3.ssamantle.application.achievement.port.out.SaveUserAchievementPort;
import com.pigeon3.ssamantle.application.common.exception.ApplicationException;
import com.pigeon3.ssamantle.application.common.exception.ExceptionType;
import com.pigeon3.ssamantle.application.user.port.out.LoadUserByIdPort;
import com.pigeon3.ssamantle.domain.model.achievement.AchievementType;
import com.pigeon3.ssamantle.domain.model.achievement.UserAchievement;
import com.pigeon3.ssamantle.domain.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 업적 체크 및 부여 서비스
 */
@Service
@RequiredArgsConstructor
public class CheckAndGrantAchievementsService implements CheckAndGrantAchievementsUseCase {

    private final LoadUserByIdPort loadUserByIdPort;
    private final LoadUserAchievementsPort loadUserAchievementsPort;
    private final LoadTotalSolvedCountPort loadTotalSolvedCountPort;
    private final SaveUserAchievementPort saveUserAchievementPort;

    @Transactional
    @Override
    public List<AchievementType> execute(CheckAndGrantAchievementsCommand command) {
        // 1. User 조회 (스트릭 정보)
        User user = loadUserByIdPort.loadById(command.userId())
                .orElseThrow(() -> ApplicationException.of(ExceptionType.USER_NOT_FOUND));

        // 2. 총 해결 문제 수 조회
        int totalSolved = loadTotalSolvedCountPort.loadTotalSolvedCount(command.userId());

        // 3. 기존 업적 조회
        List<UserAchievement> existingAchievements = loadUserAchievementsPort.loadByUserId(command.userId());
        List<AchievementType> existingTypes = existingAchievements.stream()
                .map(UserAchievement::getAchievementType)
                .toList();

        // 4. 새 업적 평가 (도메인 로직)
        List<AchievementType> newAchievementTypes = AchievementType.evaluateNewAchievements(
                user.getNowCont(),
                totalSolved,
                existingTypes
        );

        // 5. 새 업적 저장
        for (AchievementType type : newAchievementTypes) {
            UserAchievement achievement = UserAchievement.create(command.userId(), type);
            saveUserAchievementPort.save(achievement);
        }

        // 6. 새 업적 타입 목록 반환
        return newAchievementTypes;
    }
}
