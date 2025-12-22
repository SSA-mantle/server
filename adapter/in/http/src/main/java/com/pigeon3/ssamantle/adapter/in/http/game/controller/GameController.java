package com.pigeon3.ssamantle.adapter.in.http.game.controller;

import com.pigeon3.ssamantle.adapter.in.http.common.response.ApiResponse;
import com.pigeon3.ssamantle.adapter.in.http.game.dto.*;
import com.pigeon3.ssamantle.application.game.port.in.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameController {

    private final SubmitGuessUseCase submitGuessUseCase;
    private final GiveUpGameUseCase giveUpGameUseCase;
    private final GetAnswerHistoryUseCase getAnswerHistoryUseCase;

    /**
     * 단어 추측 제출
     * POST /api/v1/games/guess
     */
    @PostMapping("/guess")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<SubmitGuessResponseDto> submitGuess(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody SubmitGuessRequest request) {

        SubmitGuessCommand command = new SubmitGuessCommand(userId, request.word(), request.failCount());
        SubmitGuessResponse response = submitGuessUseCase.execute(command);

        return ApiResponse.success(SubmitGuessResponseDto.from(response));
    }

    /**
     * 게임 포기
     * POST /api/v1/games/give-up
     */
    @PostMapping("/give-up")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<GiveUpGameResponseDto> giveUp(
            @AuthenticationPrincipal Long userId) {

        GiveUpGameCommand command = new GiveUpGameCommand(userId);
        GiveUpGameResponse response = giveUpGameUseCase.execute(command);

        return ApiResponse.success(GiveUpGameResponseDto.from(response));
    }

    /**
     * 오늘 정답 및 유사도 상위 100개 단어 조회
     * GET /api/v1/games/answer-history/today
     * - 문제를 풀었거나 포기해야만 조회 가능
     */
    @GetMapping("/answer-history/today")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<GetAnswerHistoryResponseDto> getTodayAnswerHistory(
            @AuthenticationPrincipal Long userId) {

        GetAnswerHistoryCommand command = new GetAnswerHistoryCommand(userId, GetAnswerHistoryCommand.DateType.TODAY);
        GetAnswerHistoryResponse response = getAnswerHistoryUseCase.execute(command);

        return ApiResponse.success(GetAnswerHistoryResponseDto.from(response));
    }

    /**
     * 어제 정답 및 유사도 상위 100개 단어 조회
     * GET /api/v1/games/answer-history/yesterday
     * - 권한 확인 없이 조회 가능
     */
    @GetMapping("/answer-history/yesterday")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<GetAnswerHistoryResponseDto> getYesterdayAnswerHistory(
            @AuthenticationPrincipal Long userId) {

        GetAnswerHistoryCommand command = new GetAnswerHistoryCommand(userId, GetAnswerHistoryCommand.DateType.YESTERDAY);
        GetAnswerHistoryResponse response = getAnswerHistoryUseCase.execute(command);

        return ApiResponse.success(GetAnswerHistoryResponseDto.from(response));
    }
}
