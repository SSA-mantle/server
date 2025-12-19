package com.pigeon3.ssamantle.application.leaderboard.port.in;

public interface GetLeaderboardUseCase {
    GetLeaderboardResponse execute(GetLeaderboardCommand command);
}
