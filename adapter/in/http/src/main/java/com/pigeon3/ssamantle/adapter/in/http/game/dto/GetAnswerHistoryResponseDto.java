package com.pigeon3.ssamantle.adapter.in.http.game.dto;

import com.pigeon3.ssamantle.application.game.port.in.GetAnswerHistoryResponse;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record GetAnswerHistoryResponseDto(
    LocalDate date,
    String answer,
    List<WordSimilarityInfoDto> top100Words
) {
    @Builder
    public record WordSimilarityInfoDto(
        String word,
        double similarity,
        int rank
    ) {}

    public static GetAnswerHistoryResponseDto from(GetAnswerHistoryResponse response) {
        List<WordSimilarityInfoDto> top100 = null;
        if (response.top100Words() != null) {
            top100 = response.top100Words().stream()
                .map(ws -> WordSimilarityInfoDto.builder()
                    .word(ws.word())
                    .similarity(ws.similarity())
                    .rank(ws.rank())
                    .build())
                .toList();
        }

        return GetAnswerHistoryResponseDto.builder()
            .date(response.date())
            .answer(response.answer())
            .top100Words(top100)
            .build();
    }
}
