package com.pigeon3.ssamantle.domain.model.game.exception;

import lombok.Getter;

/**
 * Game 도메인 예외 타입 정의
 */
@Getter
public enum GameDomainExceptionType {

    // 추론 서버 관련 예외
    WORD_NOT_FOUND("GAME_001", "없는 단어입니다."),
    INFERENCE_SERVER_NO_RESPONSE("GAME_002", "추론 서버로부터 응답을 받지 못했습니다."),
    INFERENCE_SERVER_CALL_FAILED("GAME_003", "추론 서버 호출에 실패했습니다.");

    private final String errorCode;
    private final String message;

    GameDomainExceptionType(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
