package com.pigeon3.ssamantle.application.game.port.out;

import com.pigeon3.ssamantle.domain.model.record.Record;
import java.util.Optional;

public interface LoadTodayRecordPort {
    /**
     * 사용자의 특정 문제에 대한 기록 조회
     */
    Optional<Record> loadByUserIdAndProblemId(Long userId, Long problemId);
}
