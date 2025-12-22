package com.pigeon3.ssamantle.application.achievement.port.in;

import com.pigeon3.ssamantle.domain.model.achievement.AchievementType;
import com.pigeon3.ssamantle.domain.model.achievement.UserAchievement;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 사용자 업적 조회 응답
 */
@Builder
public record GetUserAchievementsResponse(
        Long userId,
        List<AchievementInfo> achievements
) {
    public static GetUserAchievementsResponse of(Long userId, List<UserAchievement> achievements) {
        List<AchievementInfo> achievementInfos = achievements.stream()
                .map(AchievementInfo::from)
                .toList();

        return GetUserAchievementsResponse.builder()
                .userId(userId)
                .achievements(achievementInfos)
                .build();
    }

    /**
     * 업적 정보 DTO (도메인 모델 노출 방지)
     */
    public record AchievementInfo(
            String type,
            String title,
            String description,
            LocalDateTime unlockedAt
    ) {
        public static AchievementInfo from(UserAchievement achievement) {
            return new AchievementInfo(
                    achievement.getAchievementType().name(),
                    achievement.getAchievementType().getTitle(),
                    achievement.getAchievementType().getDescription(),
                    achievement.getUnlockedAt()
            );
        }
    }
}
