package com.pigeon3.ssamantle.adapter.out.inference.similarity.dto;

/**
 * 추론 서버 유사도 계산 요청 DTO
 */
public record SimilarityRequest(
    String word
) {
    public static SimilarityRequest of(String word) {
        return new SimilarityRequest(word);
    }
}