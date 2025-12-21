package com.pigeon3.ssamantle.application.maintenance.service;

import com.pigeon3.ssamantle.application.maintenance.port.in.RunDailyMaintenanceBatchUseCase;
import com.pigeon3.ssamantle.application.leaderboard.port.out.ClearLeaderboardPort;
import com.pigeon3.ssamantle.application.leaderboard.port.out.LoadLeaderboardPort;
import com.pigeon3.ssamantle.application.leaderboard.port.out.LoadUsersByIdsPort;
import com.pigeon3.ssamantle.application.maintenance.port.in.RunDailyMaintenanceBatchCommand;
import com.pigeon3.ssamantle.application.maintenance.port.out.MaintenanceModePort;
import com.pigeon3.ssamantle.application.user.port.out.ResetTodaySolveForAllUsersPort;
import com.pigeon3.ssamantle.application.user.port.out.UpdateUserPort;
import com.pigeon3.ssamantle.domain.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RunDailyMaintenanceBatchService implements RunDailyMaintenanceBatchUseCase {

    private static final int TOP_RANKERS_LIMIT = 100;
    private static final Duration MAINTENANCE_TTL = Duration.ofMinutes(10);

    private final MaintenanceModePort maintenanceModePort;

    private final LoadLeaderboardPort loadLeaderboardPort;
    private final LoadUsersByIdsPort loadUsersByIdsPort;
    private final UpdateUserPort updateUserPort;

    private final ClearLeaderboardPort clearLeaderboardPort;

    private final ResetTodaySolveForAllUsersPort resetTodaySolveForAllUsersPort;

    /**
     * 실행 순서:
     * 1) maintenance ON (API 차단)
     * 2) Top100 조회 → (추후) 유저 최고기록 갱신
     * 3) 리더보드 초기화
     * 4) 전 유저 resetTodaySolve() (DB bulk update)
     * 5) maintenance OFF
     */
    @Transactional
    @Override
    public void execute(RunDailyMaintenanceBatchCommand command) {
        LocalDate targetDate = command.targetDate();

        try {
            // 1) Top100 조회
            List<LoadLeaderboardPort.LeaderboardRankDto> topRankers =
                    loadLeaderboardPort.loadTopRankers(targetDate, TOP_RANKERS_LIMIT);

            // 2) Top100 유저 정보 일괄 조회
            List<Long> userIds = topRankers.stream().map(LoadLeaderboardPort.LeaderboardRankDto::userId).toList();
            Map<Long, User> userMap = userIds.isEmpty() ? Map.of() : loadUsersByIdsPort.loadByIds(userIds);

            // 3) (추후) 유저 최고 기록 갱신
            // TODO: User 도메인에 "bestRank/bestRankAt" 같은 필드 + 갱신 메서드 추가 후 활성화
            // for (LoadLeaderboardPort.LeaderboardRankDto dto : topRankers) {
            //     User user = userMap.get(dto.userId());
            //     if (user == null) continue;
            //     user.updateBestRank(dto.rank(), targetDate); // <- 다음 단계에서 User에 추가
            //     updateUserPort.update(user);
            // }

            // 4) 리더보드 초기화 (Redis key delete)
            clearLeaderboardPort.clear(targetDate);

            // 5) 전 유저 resetTodaySolve() (DB UPDATE 한 방)
            resetTodaySolveForAllUsersPort.resetTodaySolveForAll();

        } finally {
            // TTL도 걸려 있지만, 정상 종료 시 즉시 해제
            maintenanceModePort.disable();
        }

    }
}
