package com.pigeon3.ssamantle.application.game.port.in;

/**
 * 게임 상태 조회 유스케이스
 * 사용자의 오늘 게임 진행 상태를 조회
 */
public interface GetGameStatusUseCase {
    GetGameStatusResponse execute(GetGameStatusQuery query);
}
