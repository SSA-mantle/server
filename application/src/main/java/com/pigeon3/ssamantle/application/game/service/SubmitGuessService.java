package com.pigeon3.ssamantle.application.game.service;

import com.pigeon3.ssamantle.application.common.exception.ApplicationException;
import com.pigeon3.ssamantle.application.common.exception.ExceptionType;
import com.pigeon3.ssamantle.application.game.port.in.*;
import com.pigeon3.ssamantle.application.game.port.out.*;
import com.pigeon3.ssamantle.application.user.port.out.LoadUserByIdPort;
import com.pigeon3.ssamantle.application.user.port.out.UpdateUserPort;
import com.pigeon3.ssamantle.domain.model.game.vo.WordSimilarity;
import com.pigeon3.ssamantle.domain.model.problem.Problem;
import com.pigeon3.ssamantle.domain.model.record.Record;
import com.pigeon3.ssamantle.domain.model.record.exception.RecordDomainException;
import com.pigeon3.ssamantle.domain.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SubmitGuessService implements SubmitGuessUseCase {

    private final LoadTodayProblemPort loadTodayProblemPort;
    private final LoadTodayRecordPort loadTodayRecordPort;
    private final SaveRecordPort saveRecordPort;
    private final UpdateRecordPort updateRecordPort;
    private final LoadUserByIdPort loadUserByIdPort;
    private final UpdateUserPort updateUserPort;
    private final LoadSimilarityFromRedisPort loadSimilarityFromRedisPort;
    private final SaveSimilarityToRedisPort saveSimilarityToRedisPort;
    private final CalculateSimilarityPort calculateSimilarityPort;

    @Transactional
    @Override
    public SubmitGuessResponse execute(SubmitGuessCommand command) {
        // 1. 오늘의 문제 조회
        LocalDate today = LocalDate.now();
        Problem todayProblem = loadTodayProblemPort.loadByDate(today)
            .orElseThrow(() -> ApplicationException.of(ExceptionType.PROBLEM_NOT_FOUND));

        // 2. 사용자 기록 조회 또는 생성
        Record record = loadTodayRecordPort.loadByUserIdAndProblemId(command.userId(), todayProblem.getId())
            .orElseGet(() -> {
                // 기록이 없으면 새로 생성
                Record newRecord = Record.create(command.userId(), todayProblem.getId());
                return saveRecordPort.save(newRecord);
            });

        // 3. 기록 상태 검증 (도메인 예외를 애플리케이션 예외로 변환)
        validateRecordState(record);

        // 4. 정답 여부 확인
        if (todayProblem.isCorrectAnswer(command.guessWord())) {
            return handleCorrectAnswer(record, todayProblem, command);
        }

        // 5. 오답 처리
        return handleWrongAnswer(record, todayProblem, command, today);
    }

    /**
     * 기록 상태 검증
     */
    private void validateRecordState(Record record) {
        if (record.isSolved()) {
            throw ApplicationException.of(ExceptionType.ALREADY_SOLVED);
        }
        if (record.isGaveUp()) {
            throw ApplicationException.of(ExceptionType.ALREADY_GAVE_UP);
        }
    }

    /**
     * 정답 처리
     */
    private SubmitGuessResponse handleCorrectAnswer(Record record, Problem problem, SubmitGuessCommand command) {
        // 1. Record 업데이트
        record.updateFailCount(command.failCount());
        record.solve();
        Record updatedRecord = updateRecordPort.update(record);

        // 2. User 업데이트
        User user = loadUserByIdPort.loadById(command.userId())
            .orElseThrow(() -> ApplicationException.of(ExceptionType.USER_NOT_FOUND));
        user.solveProblem();
        updateUserPort.update(user);

        // 3. 응답 반환
        return SubmitGuessResponse.correct(
            command.guessWord(),
            problem.getAnswer(),
            updatedRecord.getFailCount()
        );
    }

    /**
     * 오답 처리
     */
    private SubmitGuessResponse handleWrongAnswer(Record record, Problem problem, SubmitGuessCommand command, LocalDate today) {
        // 1. 유사도 조회 (Redis -> 추론 서버)
        WordSimilarity wordSimilarity = loadSimilarityFromRedisPort.loadSimilarity(today, command.guessWord())
            .orElseGet(() -> {
                // Redis에 없으면 추론 서버에 요청하고 그대로 반환
                return calculateSimilarityPort.calculate(
                    problem.getAnswer(),
                    command.guessWord()
                );
            });

        // 2. 응답 반환 (failCount는 프론트엔드에서 관리)
        return SubmitGuessResponse.wrong(wordSimilarity, record.getFailCount());
    }
}