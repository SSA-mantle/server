package com.pigeon3.ssamantle.application.game.port.out;

import com.pigeon3.ssamantle.domain.model.game.vo.WordSimilarity;
import java.time.LocalDate;
import java.util.List;

public interface LoadTop100WordsPort {
    /**
     * Redis Sorted Set에서 유사도 상위 100개 단어 조회
     * Key: "ssamantle:{date}:topk" (ZSET)
     * - ZREVRANGE로 상위 100개 조회 (내림차순, 0-based)
     * - 각 단어의 유사도와 랭킹을 포함
     */
    List<WordSimilarity> loadTop100Words(LocalDate date);
}
