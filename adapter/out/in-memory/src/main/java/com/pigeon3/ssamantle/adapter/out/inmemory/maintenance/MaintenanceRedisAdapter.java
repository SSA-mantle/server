package com.pigeon3.ssamantle.adapter.out.inmemory.maintenance;

import com.pigeon3.ssamantle.application.maintenance.port.out.MaintenanceModePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MaintenanceRedisAdapter implements MaintenanceModePort {

    private static final String KEY_MAINTENANCE = "ssamantle:maintenance";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void enable(Duration ttl, String reason) {
        // reason을 value로 저장(운영 시 확인용). TTL로 혹시 모를 영구 차단 방지
        redisTemplate.opsForValue().set(KEY_MAINTENANCE, reason, ttl);
    }

    @Override
    public void disable() {
        redisTemplate.delete(KEY_MAINTENANCE);
    }

    @Override
    public boolean isEnabled() {
        Boolean exists = redisTemplate.hasKey(KEY_MAINTENANCE);
        return Boolean.TRUE.equals(exists);
    }

    @Override
    public Optional<String> getReason() {
        Object value = redisTemplate.opsForValue().get(KEY_MAINTENANCE);
        if (value == null) return Optional.empty();
        return Optional.of(String.valueOf(value));
    }
}
