package com.pigeon3.ssamantle.adapter.in.http.game.dto;

import com.pigeon3.ssamantle.application.game.port.in.SubmitGuessResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record SubmitGuessResponseDto(
    boolean isCorrect,
    String message,
    String word,
    Double similarity,
    Integer rank,
    String answer,
    int failCount,
    List<AchievementDto> newAchievements
) {
    public static SubmitGuessResponseDto from(SubmitGuessResponse response) {
        List<AchievementDto> achievementDtos = response.newAchievements() != null
                ? response.newAchievements().stream()
                    .map(AchievementDto::from)
                    .toList()
                : null;

        return SubmitGuessResponseDto.builder()
            .isCorrect(response.isCorrect())
            .message(response.message())
            .word(response.word())
            .similarity(response.similarity())
            .rank(response.rank())
            .answer(response.answer())
            .failCount(response.failCount())
            .newAchievements(achievementDtos)
            .build();
    }

    public record AchievementDto(
        String type,
        String title,
        String description
    ) {
        public static AchievementDto from(SubmitGuessResponse.AchievementInfo info) {
            return new AchievementDto(
                info.type(),
                info.title(),
                info.description()
            );
        }
    }
}
