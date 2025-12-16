package com.pigeon3.ssamantle.adapter.in.http.game.dto;

import com.pigeon3.ssamantle.application.game.port.in.SubmitGuessResponse;
import lombok.Builder;

@Builder
public record SubmitGuessResponseDto(
    boolean isCorrect,
    String message,
    String word,
    Double similarity,
    Integer rank,
    String answer,
    int failCount
) {
    public static SubmitGuessResponseDto from(SubmitGuessResponse response) {
        return SubmitGuessResponseDto.builder()
            .isCorrect(response.isCorrect())
            .message(response.message())
            .word(response.word())
            .similarity(response.similarity())
            .rank(response.rank())
            .answer(response.answer())
            .failCount(response.failCount())
            .build();
    }
}
