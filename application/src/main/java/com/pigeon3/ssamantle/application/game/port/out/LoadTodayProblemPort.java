package com.pigeon3.ssamantle.application.game.port.out;

import com.pigeon3.ssamantle.domain.model.problem.Problem;
import java.time.LocalDate;
import java.util.Optional;

public interface LoadTodayProblemPort {
    /**
     * 특정 날짜의 문제 조회
     */
    Optional<Problem> loadByDate(LocalDate date);
}
