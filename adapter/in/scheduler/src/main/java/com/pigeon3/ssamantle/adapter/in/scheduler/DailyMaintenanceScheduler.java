package com.pigeon3.ssamantle.adapter.in.scheduler;

import com.pigeon3.ssamantle.application.maintenance.port.in.RunDailyMaintenanceBatchCommand;
import com.pigeon3.ssamantle.application.maintenance.port.in.RunDailyMaintenanceBatchUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 매일 00:00에 전날 리더보드를 기준으로 유지보수 배치를 실행한다.
 */
@Component
@RequiredArgsConstructor
public class DailyMaintenanceScheduler {

    private final RunDailyMaintenanceBatchUseCase runDailyMaintenanceBatchUseCase;

    /**
     * Asia/Seoul 기준 매일 00:00 실행.
     * - cron: second minute hour day month weekday
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void runDailyMaintenance() {
        runDailyMaintenanceBatchUseCase.execute(RunDailyMaintenanceBatchCommand.forYesterday());
    }
}
