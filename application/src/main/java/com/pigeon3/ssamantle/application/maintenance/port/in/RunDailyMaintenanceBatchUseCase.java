package com.pigeon3.ssamantle.application.maintenance.port.in;

/**
 * 매일 특정 시각(예: 00:00)에 실행되는 유지보수 배치 유스케이스.
 *
 * 요구사항(한 번의 실행에서 순서대로):
 * 1) API 전면 차단(maintenance ON)
 * 2) 리더보드 기반 유저 최고 기록 갱신 & 리더보드 초기화
 * 3) 전체 유저 resetTodaySolve() 실행
 * 4) API 차단 해제(maintenance OFF)
 */
public interface RunDailyMaintenanceBatchUseCase {

    void execute(RunDailyMaintenanceBatchCommand command);
}
