package com.pigeon3.ssamantle.application.maintenance.port.out;

import java.time.Duration;
import java.util.Optional;

/**
 * 점검(maintenance) 모드 상태를 외부 저장소(예: Redis)에 저장/조회하는 Port.
 *
 * - 배치(스케줄러)는 enable/disable을 호출해서 점검 모드를 켜고/끈다.
 * - HTTP 필터는 isEnabled()/getReason()으로 차단 여부를 판단한다.
 */
public interface MaintenanceModePort {

    /**
     * 점검 모드를 활성화한다.
     * @param ttl    배치가 중간에 죽어도 자동 해제되도록 TTL을 권장
     * @param reason 운영/디버깅용 사유
     */
    void enable(Duration ttl, String reason);

    /**
     * 점검 모드를 비활성화한다.
     */
    void disable();

    /**
     * 점검 모드가 활성화되어 있는지.
     */
    boolean isEnabled();

    /**
     * 점검 사유(있으면) 조회.
     */
    Optional<String> getReason();
}
