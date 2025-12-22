package com.pigeon3.ssamantle.application.game.port.in;

import lombok.Builder;
import java.time.LocalDate;
import java.util.List;

@Builder
public record GetAnswerHistoryResponse(
    LocalDate date,
    String answer,                          // 정답 단어 (오늘 문제를 아직 안 풀었을 때는 null)
    List<WordSimilarityInfo> top100Words    // 유사도 상위 100개 단어 (null일 수도 있음)
) {
    @Builder
    public record WordSimilarityInfo(
        String word,
        double similarity,
        int rank
    ) {}
}
