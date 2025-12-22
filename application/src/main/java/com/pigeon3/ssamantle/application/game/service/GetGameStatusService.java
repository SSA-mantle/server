package com.pigeon3.ssamantle.application.game.service;

import com.pigeon3.ssamantle.application.game.port.in.GetGameStatusQuery;
import com.pigeon3.ssamantle.application.game.port.in.GetGameStatusResponse;
import com.pigeon3.ssamantle.application.game.port.in.GetGameStatusUseCase;
import com.pigeon3.ssamantle.application.game.port.out.LoadTodayProblemPort;
import com.pigeon3.ssamantle.application.game.port.out.LoadTodayRecordPort;
import com.pigeon3.ssamantle.domain.model.problem.Problem;
import com.pigeon3.ssamantle.domain.model.record.Record;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GetGameStatusService implements GetGameStatusUseCase {

    private final LoadTodayProblemPort loadTodayProblemPort;
    private final LoadTodayRecordPort loadTodayRecordPort;

    @Transactional(readOnly = true)
    @Override
    public GetGameStatusResponse execute(GetGameStatusQuery query) {
        LocalDate today = LocalDate.now();

        // 1. 오늘의 문제 조회
        Problem todayProblem = loadTodayProblemPort.loadByDate(today)
            .orElse(null);

        // 2. 문제가 없으면 NOT_STARTED
        if (todayProblem == null) {
            return GetGameStatusResponse.notStarted();
        }

        // 3. 사용자의 오늘 기록 조회
        Record record = loadTodayRecordPort.loadByUserIdAndProblemId(query.userId(), todayProblem.getId())
            .orElse(null);

        // 4. 기록이 없으면 NOT_STARTED
        if (record == null) {
            return GetGameStatusResponse.notStarted();
        }

        // 5. 기록 상태에 따라 응답 반환
        if (record.isSolved()) {
            return GetGameStatusResponse.solved(record.getFailCount());
        }

        if (record.isGaveUp()) {
            return GetGameStatusResponse.gaveUp(record.getFailCount());
        }

        return GetGameStatusResponse.inProgress(record.getFailCount());
    }
}
