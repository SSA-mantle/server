package com.pigeon3.ssamantle.application.maintenance.service;

import com.pigeon3.ssamantle.application.maintenance.port.in.RunDailyMaintenanceBatchUseCase;
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
    private static final Duration MAINTENANCE_TTL = Duration.ofMinutes(60);

    private final MaintenanceModePort maintenanceModePort;

    private final LoadLeaderboardPort loadLeaderboardPort;
    private final LoadUsersByIdsPort loadUsersByIdsPort;
    private final UpdateUserPort updateUserPort;

    private final ResetTodaySolveForAllUsersPort resetTodaySolveForAllUsersPort;

    /**
     * 실행 순서:
     * 1) maintenance ON (API 차단)
     * 2) Top100 조회 → (추후) 유저 최고기록 갱신
     * 3) 전 유저 resetTodaySolve() (DB bulk update)
     * 4) maintenance OFF
     */
    @Transactional
    @Override
    public void execute(RunDailyMaintenanceBatchCommand command) {
        LocalDate targetDate = command.targetDate(); // 보통 "어제"가 들어오게 스케줄러에서 넣음

        maintenanceModePort.enable(MAINTENANCE_TTL, "daily maintenance batch: " + targetDate);

        try {
            // 1) 전날 Top100 조회
            List<LoadLeaderboardPort.LeaderboardRankDto> topRankers =
                    loadLeaderboardPort.loadTopRankers(targetDate, TOP_RANKERS_LIMIT);

            if (!topRankers.isEmpty()) {
                // 2) Top100 유저 일괄 조회
                List<Long> userIds = topRankers.stream()
                        .map(LoadLeaderboardPort.LeaderboardRankDto::userId)
                        .toList();

                Map<Long, User> userMap = loadUsersByIdsPort.loadByIds(userIds);

                // 3) bestRank 갱신 후 저장
                for (LoadLeaderboardPort.LeaderboardRankDto dto : topRankers) {
                    User user = userMap.get(dto.userId());
                    if (user == null) continue;

                    user.updateBestRank(dto.rank());
                    updateUserPort.update(user);
                }
            }

            // 4) 전 유저 resetTodaySolve()
            resetTodaySolveForAllUsersPort.resetTodaySolveForAll();

        } finally {
            maintenanceModePort.disable();
        }
    }
}
