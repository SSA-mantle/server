package com.pigeon3.ssamantle.application.game.service;

import com.pigeon3.ssamantle.application.achievement.port.in.CheckAndGrantAchievementsCommand;
import com.pigeon3.ssamantle.application.achievement.port.in.CheckAndGrantAchievementsUseCase;
import com.pigeon3.ssamantle.application.common.exception.ApplicationException;
import com.pigeon3.ssamantle.application.common.exception.ExceptionType;
import com.pigeon3.ssamantle.application.game.port.in.*;
import com.pigeon3.ssamantle.application.game.port.out.*;
import com.pigeon3.ssamantle.application.leaderboard.port.out.SaveLeaderboardPort;
import com.pigeon3.ssamantle.application.user.port.out.LoadUserByIdPort;
import com.pigeon3.ssamantle.application.user.port.out.UpdateUserPort;
import com.pigeon3.ssamantle.domain.model.achievement.AchievementType;
import com.pigeon3.ssamantle.domain.model.game.vo.WordSimilarity;
import com.pigeon3.ssamantle.domain.model.leaderboard.vo.LeaderboardScore;
import com.pigeon3.ssamantle.domain.model.problem.Problem;
import com.pigeon3.ssamantle.domain.model.record.Record;
import com.pigeon3.ssamantle.domain.model.record.exception.RecordDomainException;
import com.pigeon3.ssamantle.domain.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmitGuessService implements SubmitGuessUseCase {

    private final LoadTodayProblemPort loadTodayProblemPort;
    private final SaveProblemPort saveProblemPort;
    private final LoadTodayRecordPort loadTodayRecordPort;
    private final SaveRecordPort saveRecordPort;
    private final UpdateRecordPort updateRecordPort;
    private final LoadUserByIdPort loadUserByIdPort;
    private final UpdateUserPort updateUserPort;
    private final LoadWordFromTop1000Port loadWordFromTop1000Port;
    private final CalculateSimilarityPort calculateSimilarityPort;
    private final SaveLeaderboardPort saveLeaderboardPort;
    private final LoadAnswerFromRedisPort loadAnswerFromRedisPort;
    private final CheckAndGrantAchievementsUseCase checkAndGrantAchievementsUseCase;

    @Transactional
    @Override
    public SubmitGuessResponse execute(SubmitGuessCommand command) {
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

        // 3. 사용자 기록 조회 또는 생성
        Record record = loadTodayRecordPort.loadByUserIdAndProblemId(command.userId(), todayProblem.getId())
            .orElseGet(() -> {
                // 기록이 없으면 새로 생성
                Record newRecord = Record.create(command.userId(), todayProblem.getId());
                return saveRecordPort.save(newRecord);
            });

        // 4. 기록 상태 검증 (도메인 예외를 애플리케이션 예외로 변환)
        validateRecordState(record);

        // 5. 정답 여부 확인
        if (finalAnswer.equals(command.guessWord())) {
            return handleCorrectAnswer(record, finalAnswer, todayProblem.getDate(), command);
        }

        // 6. 오답 처리
        return handleWrongAnswer(record, finalAnswer, command, today);
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
    private SubmitGuessResponse handleCorrectAnswer(Record record, String answer, LocalDate problemDate, SubmitGuessCommand command) {
        // 1. Record 업데이트
        record.updateFailCount(command.failCount());
        record.solve();
        Record updatedRecord = updateRecordPort.update(record);

        // 2. User 업데이트
        User user = loadUserByIdPort.loadById(command.userId())
            .orElseThrow(() -> ApplicationException.of(ExceptionType.USER_NOT_FOUND));
        user.solveProblem();
        updateUserPort.update(user);

        // 3. 리더보드 업데이트
        updateLeaderboard(problemDate, updatedRecord);

        // 4. 업적 체크 및 부여
        CheckAndGrantAchievementsCommand achievementCommand = new CheckAndGrantAchievementsCommand(command.userId());
        List<AchievementType> newAchievements = checkAndGrantAchievementsUseCase.execute(achievementCommand);

        // 5. 응답 반환 (새 업적 포함)
        return SubmitGuessResponse.correct(
            command.guessWord(),
            answer,
            updatedRecord.getFailCount(),
            newAchievements
        );
    }

    /**
     * 리더보드 업데이트
     */
    private void updateLeaderboard(LocalDate problemDate, Record record) {
        LeaderboardScore score = LeaderboardScore.of(
            record.getFailCount(),
            record.getSolvedAt()
        );

        saveLeaderboardPort.saveOrUpdate(
            problemDate,
            record.getUserId(),
            score.getScore()
        );
    }

    /**
     * 오답 처리
     */
    private SubmitGuessResponse handleWrongAnswer(Record record, String answer, SubmitGuessCommand command, LocalDate today) {
        // 1. 유사도 조회 (파이썬 서버가 저장한 Top 1000 -> 추론 서버)
        WordSimilarity wordSimilarity = loadWordFromTop1000Port.loadWord(today, command.guessWord())
            .orElseGet(() -> {
                // Top 1000에 없으면 추론 서버에 요청
                return calculateSimilarityPort.calculate(
                    answer,
                    command.guessWord()
                );
            });

        // 2. 응답 반환 (failCount는 프론트엔드에서 관리)
        return SubmitGuessResponse.wrong(wordSimilarity, record.getFailCount());
    }
}