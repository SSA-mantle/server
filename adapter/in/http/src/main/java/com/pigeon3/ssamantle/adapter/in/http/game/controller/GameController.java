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
}
