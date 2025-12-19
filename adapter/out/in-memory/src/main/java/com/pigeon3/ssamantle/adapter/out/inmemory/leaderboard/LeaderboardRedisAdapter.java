package com.pigeon3.ssamantle.adapter.out.inmemory.leaderboard;

import com.pigeon3.ssamantle.application.leaderboard.port.out.LoadLeaderboardPort;
import com.pigeon3.ssamantle.application.leaderboard.port.out.SaveLeaderboardPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class LeaderboardRedisAdapter implements
        LoadLeaderboardPort,
        SaveLeaderboardPort {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String LEADERBOARD_KEY_PREFIX = "leaderboard:%s";
    private static final Duration CACHE_DURATION = Duration.ofDays(30); // 30일 보관
    private static final long FAIL_COUNT_MULTIPLIER = 10_000_000_000_000L; // 10^13

    @Override
    public List<LeaderboardRankDto> loadTopRankers(LocalDate date, int limit) {
        String key = getLeaderboardKey(date);

        // ZRANGE로 score 오름차순 조회 (score 낮을수록 상위)
        Set<ZSetOperations.TypedTuple<Object>> results =
            redisTemplate.opsForZSet().rangeWithScores(key, 0, limit - 1);

        if (results == null || results.isEmpty()) {
            return List.of();
        }

        List<LeaderboardRankDto> rankings = new ArrayList<>();
        int rank = 1;
        for (ZSetOperations.TypedTuple<Object> result : results) {
            Long userId = Long.parseLong(result.getValue().toString());
            double score = result.getScore();

            rankings.add(parseRankDto(userId, rank, score));
            rank++;
        }

        return rankings;
    }

    @Override
    public Optional<LeaderboardRankDto> loadUserRank(LocalDate date, Long userId) {
        String key = getLeaderboardKey(date);
        String member = userId.toString();

        // 1. 사용자가 리더보드에 있는지 확인
        Double score = redisTemplate.opsForZSet().score(key, member);
        if (score == null) {
            return Optional.empty();
        }

        // 2. 순위 조회 (0-based index이므로 +1)
        Long zeroBasedRank = redisTemplate.opsForZSet().rank(key, member);
        if (zeroBasedRank == null) {
            return Optional.empty();
        }

        int rank = zeroBasedRank.intValue() + 1;
        return Optional.of(parseRankDto(userId, rank, score));
    }

    @Override
    public void saveOrUpdate(LocalDate date, Long userId, double score) {
        String key = getLeaderboardKey(date);
        String member = userId.toString();

        redisTemplate.opsForZSet().add(key, member, score);
        redisTemplate.expire(key, CACHE_DURATION);
    }

    private String getLeaderboardKey(LocalDate date) {
        return String.format(LEADERBOARD_KEY_PREFIX, date.toString());
    }

    private LeaderboardRankDto parseRankDto(Long userId, int rank, double score) {
        // score에서 failCount와 solvedAt 역산
        long scoreAsLong = (long) score;
        int failCount = (int) (scoreAsLong / FAIL_COUNT_MULTIPLIER);
        long solvedAtEpochMilli = scoreAsLong % FAIL_COUNT_MULTIPLIER;

        return new LeaderboardRankDto(userId, rank, failCount, solvedAtEpochMilli);
    }
}
