package com.pigeon3.ssamantle.application.game.service;

import com.pigeon3.ssamantle.application.common.exception.ApplicationException;
import com.pigeon3.ssamantle.application.common.exception.ExceptionType;
import com.pigeon3.ssamantle.application.game.port.in.*;
import com.pigeon3.ssamantle.application.game.port.out.*;
import com.pigeon3.ssamantle.domain.model.problem.Problem;
import com.pigeon3.ssamantle.domain.model.record.Record;
import com.pigeon3.ssamantle.domain.model.record.exception.RecordDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GiveUpGameService implements GiveUpGameUseCase {

    private final LoadTodayProblemPort loadTodayProblemPort;
    private final LoadTodayRecordPort loadTodayRecordPort;
    private final UpdateRecordPort updateRecordPort;

    @Transactional
    @Override
    public GiveUpGameResponse execute(GiveUpGameCommand command) {
        // 1. 오늘의 문제 조회
        LocalDate today = LocalDate.now();
        Problem todayProblem = loadTodayProblemPort.loadByDate(today)
            .orElseThrow(() -> ApplicationException.of(ExceptionType.PROBLEM_NOT_FOUND));

        // 2. 사용자 기록 조회
        Record record = loadTodayRecordPort.loadByUserIdAndProblemId(command.userId(), todayProblem.getId())
            .orElseThrow(() -> ApplicationException.of(ExceptionType.NO_GAME_IN_PROGRESS));

        // 3. 도메인 예외를 애플리케이션 예외로 변환하여 포기 처리
        try {
            record.giveUp();
        } catch (RecordDomainException e) {
            throw ApplicationException.of(ExceptionType.CONFLICT, e.getMessage());
        }

        // 4. Record 업데이트
        Record updatedRecord = updateRecordPort.update(record);

        // 5. 응답 반환
        return GiveUpGameResponse.of(
            todayProblem.getAnswer(),
            updatedRecord.getFailCount(),
            updatedRecord.getGiveUpAt()
        );
    }
}
