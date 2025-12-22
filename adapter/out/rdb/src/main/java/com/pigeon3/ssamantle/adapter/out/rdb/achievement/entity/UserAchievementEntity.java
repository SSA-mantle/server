package com.pigeon3.ssamantle.adapter.out.rdb.achievement.entity;

import com.pigeon3.ssamantle.domain.model.achievement.AchievementType;
import com.pigeon3.ssamantle.domain.model.achievement.UserAchievement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 사용자 업적 영속성 엔티티
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAchievementEntity {

    private Long id;
    private Long userId;
    private String achievementType;
    private LocalDateTime unlockedAt;

    /**
     * 도메인 모델로 변환
     */
    public UserAchievement toDomain() {
        return UserAchievement.reconstruct(
                this.id,
                this.userId,
                AchievementType.valueOf(this.achievementType),
                this.unlockedAt
        );
    }

    /**
     * 도메인 모델로부터 생성
     */
    public static UserAchievementEntity fromDomain(UserAchievement domain) {
        return UserAchievementEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .achievementType(domain.getAchievementType().name())
                .unlockedAt(domain.getUnlockedAt())
                .build();
    }
}
