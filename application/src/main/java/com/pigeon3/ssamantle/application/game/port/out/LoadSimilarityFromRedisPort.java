package com.pigeon3.ssamantle.application.game.port.out;

import com.pigeon3.ssamantle.domain.model.game.vo.WordSimilarity;
import java.time.LocalDate;
import java.util.Optional;

public interface LoadSimilarityFromRedisPort {
    /**
     * Redis에서 단어 유사도 조회
     * Key: "problem:{date}:words:{word}"
     */
    Optional<WordSimilarity> loadSimilarity(LocalDate date, String word);
}
