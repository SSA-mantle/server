package com.pigeon3.ssamantle.adapter.in.http.achievement.dto;

import com.pigeon3.ssamantle.application.achievement.port.in.GetUserAchievementsResponse;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 사용자 업적 조회 응답 DTO
 */
@Builder
public record GetUserAchievementsResponseDto(
        Long userId,
        List<AchievementDto> achievements
) {
    public static GetUserAchievementsResponseDto from(GetUserAchievementsResponse response) {
        List<AchievementDto> achievementDtos = response.achievements().stream()
                .map(AchievementDto::from)
                .toList();

        return GetUserAchievementsResponseDto.builder()
                .userId(response.userId())
                .achievements(achievementDtos)
                .build();
    }

    /**
     * 업적 DTO
     */
    @Builder
    public record AchievementDto(
            String type,
            String title,
            String description,
            LocalDateTime unlockedAt
    ) {
        public static AchievementDto from(GetUserAchievementsResponse.AchievementInfo info) {
            return AchievementDto.builder()
                    .type(info.type())
                    .title(info.title())
                    .description(info.description())
                    .unlockedAt(info.unlockedAt())
                    .build();
        }
    }
}
