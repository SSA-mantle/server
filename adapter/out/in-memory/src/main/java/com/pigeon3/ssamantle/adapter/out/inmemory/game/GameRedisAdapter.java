package com.pigeon3.ssamantle.adapter.out.inmemory.game;

import com.pigeon3.ssamantle.adapter.out.inmemory.game.dto.WordSimilarityRedisDto;
import com.pigeon3.ssamantle.application.game.port.out.LoadSimilarityFromRedisPort;
import com.pigeon3.ssamantle.application.game.port.out.SaveSimilarityToRedisPort;
import com.pigeon3.ssamantle.domain.model.game.vo.WordSimilarity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GameRedisAdapter implements
        LoadSimilarityFromRedisPort,
        SaveSimilarityToRedisPort {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String WORD_SIMILARITY_KEY_PREFIX = "problem:%s:words:%s";
    private static final Duration CACHE_DURATION = Duration.ofDays(7); // 7일 캐싱

    @Override
    public Optional<WordSimilarity> loadSimilarity(LocalDate date, String word) {
        String key = String.format(WORD_SIMILARITY_KEY_PREFIX, date.toString(), word);
        Object value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            return Optional.empty();
        }

        // GenericJackson2JsonRedisSerializer가 LinkedHashMap으로 역직렬화할 수 있음
        if (value instanceof WordSimilarityRedisDto dto) {
            return Optional.of(dto.toDomain());
        }

        // LinkedHashMap인 경우 수동 변환
        if (value instanceof java.util.Map) {
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> map = (java.util.Map<String, Object>) value;
            WordSimilarityRedisDto dto = WordSimilarityRedisDto.builder()
                .word((String) map.get("word"))
                .similarity(((Number) map.get("similarity")).doubleValue())
                .rank(((Number) map.get("rank")).intValue())
                .build();
            return Optional.of(dto.toDomain());
        }

        return Optional.empty();
    }

    @Override
    public void saveSimilarity(LocalDate date, WordSimilarity wordSimilarity) {
        String key = String.format(WORD_SIMILARITY_KEY_PREFIX, date.toString(), wordSimilarity.getWord());
        WordSimilarityRedisDto dto = WordSimilarityRedisDto.from(wordSimilarity);
        redisTemplate.opsForValue().set(key, dto, CACHE_DURATION);
    }
}
