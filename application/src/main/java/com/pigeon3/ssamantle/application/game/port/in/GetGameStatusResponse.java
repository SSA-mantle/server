package com.pigeon3.ssamantle.application.game.port.in;

import com.pigeon3.ssamantle.domain.model.game.GameStatus;

/**
 * 게임 상태 조회 응답
 */
public record GetGameStatusResponse(
        String status,
        Integer failCount
) {
    public static GetGameStatusResponse notStarted() {
        return new GetGameStatusResponse(GameStatus.NOT_STARTED.name(), 0);
    }

    public static GetGameStatusResponse inProgress(int failCount) {
        return new GetGameStatusResponse(GameStatus.IN_PROGRESS.name(), failCount);
    }

    public static GetGameStatusResponse solved(int failCount) {
        return new GetGameStatusResponse(GameStatus.SOLVED.name(), failCount);
    }

    public static GetGameStatusResponse gaveUp(int failCount) {
        return new GetGameStatusResponse(GameStatus.GAVE_UP.name(), failCount);
    }
}
