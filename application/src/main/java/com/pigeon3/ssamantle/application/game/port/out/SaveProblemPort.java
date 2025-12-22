package com.pigeon3.ssamantle.application.game.port.out;

import com.pigeon3.ssamantle.domain.model.problem.Problem;

public interface SaveProblemPort {
    /**
     * 문제 저장 (새로 생성)
     * @return 저장된 문제 (id 포함)
     */
    Problem save(Problem problem);
}
