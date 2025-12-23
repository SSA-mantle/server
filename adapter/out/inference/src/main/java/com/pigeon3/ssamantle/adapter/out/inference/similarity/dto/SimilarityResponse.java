package com.pigeon3.ssamantle.adapter.out.inference.similarity.dto;

import com.pigeon3.ssamantle.domain.model.game.exception.GameDomainException;
import com.pigeon3.ssamantle.domain.model.game.exception.GameDomainExceptionType;
import com.pigeon3.ssamantle.domain.model.game.vo.WordSimilarity;

/**
 * 추론 서버 유사도 계산 응답 DTO
 * 파이썬 추론 서버의 응답 형식에 맞춤
 * similarity가 null인 경우 에러를 의미하며, reason 필드에 에러 사유가 포함됨
 */
public record SimilarityResponse(
    String date,           // 문제 날짜 (YYYY-MM-DD)
    String answer,         // 정답 단어
    String word,           // 추측 단어
    Double similarity,     // 유사도 (null이면 에러)
    String reason          // 에러 사유 (similarity가 null일 때만)
) {
    /**
     * 도메인 모델로 변환
     * similarity가 null이면 예외 발생 (없는 단어)
     * rank는 파이썬 서버가 제공하지 않으므로 -1(순위 없음)로 설정
     * similarity는 0.0~1.0 범위의 값이므로 100을 곱해서 0.0~100.0으로 변환
     */
    public WordSimilarity toDomain() {
        if (similarity == null) {
            throw GameDomainException.of(GameDomainExceptionType.WORD_NOT_FOUND);
        }
        return WordSimilarity.of(word, similarity * 100, -1);  // rank는 항상 -1
    }
}
