package com.pigeon3.ssamantle.application.game.port.in;

import com.pigeon3.ssamantle.domain.model.game.vo.WordSimilarity;
import lombok.Builder;

@Builder
public record SubmitGuessResponse(
    boolean isCorrect,          // 정답 여부
    String message,             // 응답 메시지
    String word,                // 제출한 단어
    Double similarity,          // 유사도 (정답이 아닐 때만)
    Integer rank,               // 순위 (정답이 아닐 때만)
    String answer,              // 정답 단어 (정답일 때만)
    int failCount              // 현재 실패 횟수
) {
    public static SubmitGuessResponse correct(String word, String answer, int failCount) {
        return SubmitGuessResponse.builder()
            .isCorrect(true)
            .message("정답입니다!")
            .word(word)
            .answer(answer)
            .failCount(failCount)
            .build();
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
