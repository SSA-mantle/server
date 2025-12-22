package com.pigeon3.ssamantle.application.game.port.in;

import com.pigeon3.ssamantle.domain.model.achievement.AchievementType;
import com.pigeon3.ssamantle.domain.model.game.vo.WordSimilarity;
import lombok.Builder;

import java.util.List;

@Builder
public record SubmitGuessResponse(
    boolean isCorrect,          // 정답 여부
    String message,             // 응답 메시지
    String word,                // 제출한 단어
    Double similarity,          // 유사도 (정답이 아닐 때만)
    Integer rank,               // 순위 (정답이 아닐 때만)
    String answer,              // 정답 단어 (정답일 때만)
    int failCount,              // 현재 실패 횟수
    List<AchievementInfo> newAchievements  // 새로 획득한 업적 (정답일 때만)
) {
    public static SubmitGuessResponse correct(String word, String answer, int failCount, List<AchievementType> newAchievementTypes) {
        // AchievementType을 AchievementInfo DTO로 변환
        List<AchievementInfo> achievements = newAchievementTypes != null
                ? newAchievementTypes.stream()
                    .map(type -> new AchievementInfo(
                            type.name(),
                            type.getTitle(),
                            type.getDescription()
                    ))
                    .toList()
                : null;

        return SubmitGuessResponse.builder()
            .isCorrect(true)
            .message("정답입니다!")
            .word(word)
            .answer(answer)
            .failCount(failCount)
            .newAchievements(achievements)
            .build();
    }

    /**
     * 업적 정보 DTO (도메인 모델 노출 방지)
     */
    public record AchievementInfo(
            String type,
            String title,
            String description
    ) {
    }

    public static SubmitGuessResponse wrong(WordSimilarity wordSimilarity, int failCount) {
        return SubmitGuessResponse.builder()
            .isCorrect(false)
            .message("오답입니다.")
            .word(wordSimilarity.getWord())
            .similarity(wordSimilarity.getSimilarity())
            .rank(wordSimilarity.getRank())
            .failCount(failCount)
            .build();
    }
}
