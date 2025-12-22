package com.pigeon3.ssamantle.application.user.service;

import com.pigeon3.ssamantle.application.common.exception.ApplicationException;
import com.pigeon3.ssamantle.application.common.exception.ExceptionType;
import com.pigeon3.ssamantle.application.user.port.in.GetMyGameStatisticsCommand;
import com.pigeon3.ssamantle.application.user.port.in.GetMyGameStatisticsResponse;
import com.pigeon3.ssamantle.application.user.port.in.GetMyGameStatisticsUseCase;
import com.pigeon3.ssamantle.application.user.port.out.GameStatisticsData;
import com.pigeon3.ssamantle.application.user.port.out.LoadGameStatisticsPort;
import com.pigeon3.ssamantle.application.user.port.out.LoadUserByIdPort;
import com.pigeon3.ssamantle.domain.model.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 내 게임 통계 조회 UseCase 구현
 */
@Service
@Transactional(readOnly = true)
public class GetMyGameStatisticsService implements GetMyGameStatisticsUseCase {

    private final LoadUserByIdPort loadUserByIdPort;
    private final LoadGameStatisticsPort loadGameStatisticsPort;

    public GetMyGameStatisticsService(
        LoadUserByIdPort loadUserByIdPort,
        LoadGameStatisticsPort loadGameStatisticsPort
    ) {
        this.loadUserByIdPort = loadUserByIdPort;
        this.loadGameStatisticsPort = loadGameStatisticsPort;
    }

    @Override
    public GetMyGameStatisticsResponse execute(GetMyGameStatisticsCommand command) {
        // 1. 사용자 조회
        User user = loadUserByIdPort.loadById(command.userId())
            .orElseThrow(() -> ApplicationException.of(ExceptionType.USER_NOT_FOUND));

        // 2. 삭제된 사용자 확인
        if (user.isDelete()) {
            throw ApplicationException.of(ExceptionType.USER_DELETED);
        }

        // 3. 게임 통계 조회
        GameStatisticsData statisticsData = loadGameStatisticsPort.loadStatisticsByUserId(command.userId());

        // 4. Response 생성 및 반환
        return GetMyGameStatisticsResponse.of(user, statisticsData);
    }
}
