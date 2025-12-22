package com.pigeon3.ssamantle.application.achievement.service;

import com.pigeon3.ssamantle.application.achievement.port.in.GetUserAchievementsQuery;
import com.pigeon3.ssamantle.application.achievement.port.in.GetUserAchievementsResponse;
import com.pigeon3.ssamantle.application.achievement.port.in.GetUserAchievementsUseCase;
import com.pigeon3.ssamantle.application.achievement.port.out.LoadUserAchievementsPort;
import com.pigeon3.ssamantle.application.common.exception.ApplicationException;
import com.pigeon3.ssamantle.application.common.exception.ExceptionType;
import com.pigeon3.ssamantle.application.user.port.out.LoadUserByIdPort;
import com.pigeon3.ssamantle.domain.model.achievement.UserAchievement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 사용자 업적 조회 서비스
 */
@Service
@RequiredArgsConstructor
public class GetUserAchievementsService implements GetUserAchievementsUseCase {

    private final LoadUserByIdPort loadUserByIdPort;
    private final LoadUserAchievementsPort loadUserAchievementsPort;

    @Transactional(readOnly = true)
    @Override
    public GetUserAchievementsResponse execute(GetUserAchievementsQuery query) {
        // 1. 사용자 존재 여부 확인
        loadUserByIdPort.loadById(query.userId())
                .orElseThrow(() -> ApplicationException.of(ExceptionType.USER_NOT_FOUND));

        // 2. 사용자 업적 조회
        List<UserAchievement> achievements = loadUserAchievementsPort.loadByUserId(query.userId());

        // 3. 응답 반환
        return GetUserAchievementsResponse.of(query.userId(), achievements);
    }
}
