package com.pigeon3.ssamantle.application.game.port.out;

import com.pigeon3.ssamantle.domain.model.game.vo.WordSimilarity;
import java.time.LocalDate;

public interface SaveSimilarityToRedisPort {
    /**
     * Redis에 단어 유사도 저장
     */
    void saveSimilarity(LocalDate date, WordSimilarity wordSimilarity);
}
