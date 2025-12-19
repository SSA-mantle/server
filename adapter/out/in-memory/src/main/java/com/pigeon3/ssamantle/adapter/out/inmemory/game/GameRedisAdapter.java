package com.pigeon3.ssamantle.adapter.out.inmemory.game;

import com.pigeon3.ssamantle.application.game.port.out.LoadAnswerFromRedisPort;
import com.pigeon3.ssamantle.application.game.port.out.LoadWordFromTop1000Port;
import com.pigeon3.ssamantle.domain.model.game.vo.WordSimilarity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Redis에서 파이썬 서버가 저장한 데이터를 조회하는 어댑터
 * - Top 1000 단어 조회
 * - 정답 단어 조회
 */
@Component
@RequiredArgsConstructor
public class GameRedisAdapter implements
        LoadWordFromTop1000Port,
        LoadAnswerFromRedisPort {

    private final RedisTemplate<String, Object> redisTemplate;

    // 파이썬 서버와 공유하는 키 (ssamentle 프리픽스)
    private static final String REDIS_KEY_PREFIX = "ssamentle";
    private static final String TOP1000_KEY_FORMAT = "%s:%s:topk";  // ssamentle:{date}:topk
    private static final String ANSWER_KEY_FORMAT = "%s:%s:answer"; // ssamentle:{date}:answer

    @Override
    public Optional<WordSimilarity> loadWord(LocalDate date, String word) {
        String key = String.format(TOP1000_KEY_FORMAT, REDIS_KEY_PREFIX, date.toString());

        // ZSCORE: 유사도 조회 (O(1))
        Double score = redisTemplate.opsForZSet().score(key, word);
        if (score == null) {
            return Optional.empty();  // Top 1000에 없음
        }

        // ZREVRANK: 랭킹 조회 (내림차순, 0-based) (O(log N))
        Long rank = redisTemplate.opsForZSet().reverseRank(key, word);
        if (rank == null) {
            return Optional.empty();
        }

        // score는 0.0~1.0 범위이므로 100을 곱해서 0.0~100.0으로 변환
        // rank는 0-based이므로 1을 더해서 1-based로 변환
        return Optional.of(WordSimilarity.of(word, score * 100, rank.intValue() + 1));
    }

    @Override
    public Optional<String> loadAnswer(LocalDate date) {
        String key = String.format(ANSWER_KEY_FORMAT, REDIS_KEY_PREFIX, date.toString());
        Object value = redisTemplate.opsForValue().get(key);

        if (value instanceof String answer) {
            return Optional.of(answer);
        }

        return Optional.empty();
    }
}
