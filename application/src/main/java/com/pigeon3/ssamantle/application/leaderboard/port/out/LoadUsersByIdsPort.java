package com.pigeon3.ssamantle.application.leaderboard.port.out;

import com.pigeon3.ssamantle.domain.model.user.User;

import java.util.List;
import java.util.Map;

/**
 * 여러 사용자 조회 포트
 */
public interface LoadUsersByIdsPort {
    /**
     * 여러 사용자를 ID로 조회
     * @param userIds 사용자 ID 목록
     * @return userId -> User 맵
     */
    Map<Long, User> loadByIds(List<Long> userIds);
}
