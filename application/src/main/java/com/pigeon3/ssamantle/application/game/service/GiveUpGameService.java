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
    private final SaveProblemPort saveProblemPort;
    private final LoadTodayRecordPort loadTodayRecordPort;
    private final UpdateRecordPort updateRecordPort;
    private final LoadAnswerFromRedisPort loadAnswerFromRedisPort;

    @Transactional
    @Override
    public GiveUpGameResponse execute(GiveUpGameCommand command) {
        LocalDate today = LocalDate.now();

        // 1. Redis에서 정답 조회 (먼저 시도)
        String answer = loadAnswerFromRedisPort.loadAnswer(today)
            .orElse(null);

        // 2. Problem 조회 또는 생성
        Problem todayProblem;
        final String finalAnswer;
        if (answer != null) {
            // Redis에 정답이 있으면: DB 조회 후 없으면 생성
            finalAnswer = answer;
            todayProblem = loadTodayProblemPort.loadByDate(today)
                .orElseGet(() -> {
                    // Problem이 없으면 Redis 정답으로 새로 생성
                    Problem newProblem = Problem.create(finalAnswer, today);
                    return saveProblemPort.save(newProblem);
                });
        } else {
            // Redis에 정답이 없으면: DB에서 조회 (없으면 예외)
            todayProblem = loadTodayProblemPort.loadByDate(today)
                .orElseThrow(() -> ApplicationException.of(ExceptionType.PROBLEM_NOT_FOUND));
            finalAnswer = todayProblem.getAnswer();
        }

        // 3. 사용자 기록 조회
        Record record = loadTodayRecordPort.loadByUserIdAndProblemId(command.userId(), todayProblem.getId())
            .orElseThrow(() -> ApplicationException.of(ExceptionType.NO_GAME_IN_PROGRESS));

        // 4. 도메인 예외를 애플리케이션 예외로 변환하여 포기 처리
        try {
            record.giveUp();
        } catch (RecordDomainException e) {
            throw ApplicationException.of(ExceptionType.CONFLICT, e.getMessage());
        }

        // 5. Record 업데이트
        Record updatedRecord = updateRecordPort.update(record);

        // 6. 응답 반환
        return GiveUpGameResponse.of(
            finalAnswer,
            updatedRecord.getFailCount(),
            updatedRecord.getGiveUpAt()
        );
    }
}
