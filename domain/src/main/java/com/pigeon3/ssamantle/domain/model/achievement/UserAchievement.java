package com.pigeon3.ssamantle.domain.model.achievement;

import com.pigeon3.ssamantle.domain.model.achievement.exception.AchievementDomainException;
import com.pigeon3.ssamantle.domain.model.achievement.exception.AchievementDomainExceptionType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 사용자 업적 도메인 모델
 */
@Getter
public class UserAchievement {

    private final Long id;
    private final Long userId;
    private final AchievementType achievementType;
    private final LocalDateTime unlockedAt;

    private UserAchievement(Long id, Long userId, AchievementType achievementType, LocalDateTime unlockedAt) {
        this.id = id;
        this.userId = userId;
        this.achievementType = achievementType;
        this.unlockedAt = unlockedAt;
    }

    /**
     * 새로운 업적 생성 (신규 획득)
     *
     * @param userId         사용자 ID
     * @param achievementType 업적 타입
     * @return 생성된 업적
     */
    public static UserAchievement create(Long userId, AchievementType achievementType) {
        validateUserId(userId);
        validateAchievementType(achievementType);

        return new UserAchievement(
                null,
                userId,
                achievementType,
                LocalDateTime.now()
        );
    }

    /**
     * 기존 업적 재구성 (영속성 계층에서 조회)
     *
     * @param id              업적 ID
     * @param userId         사용자 ID
     * @param achievementType 업적 타입
     * @param unlockedAt     획득 시각
     * @return 재구성된 업적
     */
    public static UserAchievement reconstruct(Long id, Long userId, AchievementType achievementType, LocalDateTime unlockedAt) {
        validateUserId(userId);
        validateAchievementType(achievementType);

        return new UserAchievement(id, userId, achievementType, unlockedAt);
    }

    /**
     * 사용자 ID 유효성 검증
     */
    private static void validateUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw AchievementDomainException.of(
                    AchievementDomainExceptionType.INVALID_USER_ID,
                    "사용자 ID는 필수이며 0보다 커야 합니다: " + userId
            );
        }
    }

    /**
     * 업적 타입 유효성 검증
     */
    private static void validateAchievementType(AchievementType achievementType) {
        if (achievementType == null) {
            throw AchievementDomainException.of(
                    AchievementDomainExceptionType.INVALID_ACHIEVEMENT_TYPE,
                    "업적 타입은 필수입니다"
            );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAchievement that = (UserAchievement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
