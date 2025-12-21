package com.pigeon3.ssamantle.application.maintenance.port.in;

import java.time.LocalDate;

/**
 * 일일 유지보수 배치 실행 커맨드
 * - targetDate: 리더보드/최고기록 갱신 기준 날짜(보통 어제)
 */
public record RunDailyMaintenanceBatchCommand(LocalDate targetDate) {

    public RunDailyMaintenanceBatchCommand {
        if (targetDate == null) {
            throw new IllegalArgumentException("targetDate는 필수입니다.");
        }
    }

    public static RunDailyMaintenanceBatchCommand forYesterday() {
        return new RunDailyMaintenanceBatchCommand(LocalDate.now().minusDays(1));
    }
}
