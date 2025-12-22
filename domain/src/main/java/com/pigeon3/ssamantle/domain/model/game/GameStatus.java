package com.pigeon3.ssamantle.domain.model.game;

/**
 * 게임 상태
 * 사용자의 오늘 게임 진행 상태를 나타냄
 */
public enum GameStatus {
    /**
     * 오늘 문제를 시작하지 않음
     */
    NOT_STARTED,

    /**
     * 문제를 풀고 있는 중
     */
    IN_PROGRESS,

    /**
     * 문제를 해결함
     */
    SOLVED,

    /**
     * 문제를 포기함
     */
    GAVE_UP
}
