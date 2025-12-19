package com.pigeon3.ssamantle.application.game.port.out;

import com.pigeon3.ssamantle.domain.model.game.vo.WordSimilarity;
import java.time.LocalDate;
import java.util.Optional;

public interface LoadWordFromTop1000Port {
    /**
     * Redis Sorted Set에서 특정 단어의 유사도와 랭킹 조회
     * Key: "ssamentle:{date}:topk" (ZSET)
     * - ZSCORE로 유사도 조회
     * - ZREVRANK로 랭킹 조회 (내림차순, 0-based)
     */
    Optional<WordSimilarity> loadWord(LocalDate date, String word);
}
