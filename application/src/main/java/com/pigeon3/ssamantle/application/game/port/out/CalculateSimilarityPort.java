package com.pigeon3.ssamantle.application.game.port.out;

import com.pigeon3.ssamantle.domain.model.game.vo.WordSimilarity;

public interface CalculateSimilarityPort {
    /**
     * 추론 서버에 단어 유사도 계산 요청
     * 임시 구현: 랜덤 유사도 반환
     */
    WordSimilarity calculate(String answerWord, String guessWord);
}
