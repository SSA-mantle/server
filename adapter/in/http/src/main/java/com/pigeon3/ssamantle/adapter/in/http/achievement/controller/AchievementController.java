package com.pigeon3.ssamantle.adapter.in.http.achievement.controller;

import com.pigeon3.ssamantle.adapter.in.http.achievement.dto.GetUserAchievementsResponseDto;
import com.pigeon3.ssamantle.adapter.in.http.common.response.ApiResponse;
import com.pigeon3.ssamantle.application.achievement.port.in.GetUserAchievementsQuery;
import com.pigeon3.ssamantle.application.achievement.port.in.GetUserAchievementsResponse;
import com.pigeon3.ssamantle.application.achievement.port.in.GetUserAchievementsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 업적 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final GetUserAchievementsUseCase getUserAchievementsUseCase;

    /**
     * 본인 업적 목록 조회
     * GET /api/v1/achievements/me
     */
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<GetUserAchievementsResponseDto> getMyAchievements(
            @AuthenticationPrincipal Long userId) {

        GetUserAchievementsQuery query = new GetUserAchievementsQuery(userId);
        GetUserAchievementsResponse response = getUserAchievementsUseCase.execute(query);

        return ApiResponse.success(GetUserAchievementsResponseDto.from(response));
    }
}
