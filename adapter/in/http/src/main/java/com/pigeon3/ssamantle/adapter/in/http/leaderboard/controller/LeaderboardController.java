package com.pigeon3.ssamantle.adapter.in.http.leaderboard.controller;

import com.pigeon3.ssamantle.adapter.in.http.common.response.ApiResponse;
import com.pigeon3.ssamantle.adapter.in.http.leaderboard.dto.GetLeaderboardResponseDto;
import com.pigeon3.ssamantle.application.leaderboard.port.in.GetLeaderboardCommand;
import com.pigeon3.ssamantle.application.leaderboard.port.in.GetLeaderboardResponse;
import com.pigeon3.ssamantle.application.leaderboard.port.in.GetLeaderboardUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final GetLeaderboardUseCase getLeaderboardUseCase;

    /**
     * 리더보드 조회
     * GET /api/v1/leaderboard?date=2025-12-19
     * date 파라미터 생략 시 오늘 날짜
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<GetLeaderboardResponseDto> getLeaderboard(
            @AuthenticationPrincipal Long userId,
            @RequestParam(required = false) LocalDate date) {

        GetLeaderboardCommand command = new GetLeaderboardCommand(userId, date);
        GetLeaderboardResponse response = getLeaderboardUseCase.execute(command);

        return ApiResponse.success(GetLeaderboardResponseDto.from(response));
    }
}
