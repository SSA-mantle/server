package com.pigeon3.ssamantle.adapter.out.inference.similarity.dto;

import com.pigeon3.ssamantle.domain.model.game.vo.WordSimilarity;

/**
 * 추론 서버 유사도 계산 응답 DTO
 * rank가 null인 경우 1000위 초과를 의미
 */
public record SimilarityResponse(
    String word,
    double similarity,
    Integer rank  // nullable - null이면 -1(순위 없음)로 처리
) {
    /**
     * 도메인 모델로 변환
     * rank가 null이면 -1(순위 없음)로 설정
     */
    public WordSimilarity toDomain() {
        int rankValue = (rank != null) ? rank : -1;
        return WordSimilarity.of(word, similarity, rankValue);
    }
}
