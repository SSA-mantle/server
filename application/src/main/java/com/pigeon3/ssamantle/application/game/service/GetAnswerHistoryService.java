package com.pigeon3.ssamantle.application.game.service;

import com.pigeon3.ssamantle.application.game.port.in.*;
import com.pigeon3.ssamantle.application.game.port.out.*;
import com.pigeon3.ssamantle.domain.model.game.vo.WordSimilarity;
import com.pigeon3.ssamantle.domain.model.problem.Problem;
import com.pigeon3.ssamantle.domain.model.record.Record;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetAnswerHistoryService implements GetAnswerHistoryUseCase {

    private final LoadTodayProblemPort loadTodayProblemPort;
    private final LoadTodayRecordPort loadTodayRecordPort;
    private final LoadTop100WordsPort loadTop100WordsPort;
    private final LoadAnswerFromRedisPort loadAnswerFromRedisPort;

    @Transactional(readOnly = true)
    @Override
    public GetAnswerHistoryResponse execute(GetAnswerHistoryCommand command) {
        // 날짜 타입에 따라 조회할 날짜 계산
        LocalDate targetDate = switch (command.dateType()) {
            case TODAY -> LocalDate.now();
            case YESTERDAY -> LocalDate.now().minusDays(1);
        };

        // 오늘인 경우에만 권한 확인 필요
        boolean isToday = command.dateType() == GetAnswerHistoryCommand.DateType.TODAY;

        // 1. Redis에서 정답 및 정답 설명 조회 (먼저 시도)
        Optional<String> answerFromRedis = loadAnswerFromRedisPort.loadAnswer(targetDate);
        Optional<String> answerDescriptionFromRedis = loadAnswerFromRedisPort.loadAnswerDescription(targetDate);

        String answer = null;
        String answerDescription = null;
        Long problemId = null;

        if (answerFromRedis.isPresent()) {
            // Redis에 정답이 있는 경우
            answer = answerFromRedis.get();
            answerDescription = answerDescriptionFromRedis.orElse(null);

            // 권한 확인을 위해 문제 ID가 필요한 경우 DB 조회
            if (isToday) {
                Optional<Problem> problemOpt = loadTodayProblemPort.loadByDate(targetDate);
                if (problemOpt.isEmpty()) {
                    return GetAnswerHistoryResponse.builder()
                        .date(targetDate)
                        .answer(null)
                        .answerDescription(null)
                        .top100Words(null)
                        .build();
                }
                problemId = problemOpt.get().getId();
            }
        } else {
            // Redis에 없으면 DB에서 문제 조회
            Optional<Problem> problemOpt = loadTodayProblemPort.loadByDate(targetDate);
            if (problemOpt.isEmpty()) {
                return GetAnswerHistoryResponse.builder()
                    .date(targetDate)
                    .answer(null)
                    .answerDescription(null)
                    .top100Words(null)
                    .build();
            }

            Problem problem = problemOpt.get();
            answer = problem.getAnswer();
            answerDescription = answerDescriptionFromRedis.orElse(null);
            problemId = problem.getId();
        }

        // 2. 오늘 문제인 경우 권한 확인 (풀었거나 포기해야만 조회 가능)
        if (isToday && problemId != null) {
            Optional<Record> recordOpt = loadTodayRecordPort.loadByUserIdAndProblemId(command.userId(), problemId);

            // 기록이 없거나 진행 중인 경우 null 반환
            if (recordOpt.isEmpty() || recordOpt.get().isInProgress()) {
                return GetAnswerHistoryResponse.builder()
                    .date(targetDate)
                    .answer(null)
                    .answerDescription(null)
                    .top100Words(null)
                    .build();
            }
        }

        // 3. 유사도 상위 100개 조회
        List<WordSimilarity> top100 = loadTop100WordsPort.loadTop100Words(targetDate);
        List<GetAnswerHistoryResponse.WordSimilarityInfo> top100Info = top100.stream()
            .map(ws -> GetAnswerHistoryResponse.WordSimilarityInfo.builder()
                .word(ws.getWord())
                .similarity(ws.getSimilarity())
                .rank(ws.getRank())
                .build())
            .toList();

        return GetAnswerHistoryResponse.builder()
            .date(targetDate)
            .answer(answer)
            .answerDescription(answerDescription)
            .top100Words(top100Info)
            .build();
    }
}
