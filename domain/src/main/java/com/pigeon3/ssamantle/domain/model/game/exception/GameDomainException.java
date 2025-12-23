package com.pigeon3.ssamantle.domain.model.game.exception;

import lombok.Getter;

/**
 * Game 도메인 예외
 */
@Getter
public class GameDomainException extends RuntimeException {

    private final GameDomainExceptionType exceptionType;

    private GameDomainException(GameDomainExceptionType exceptionType, String message) {
        super(message);
        this.exceptionType = exceptionType;
    }

    private GameDomainException(GameDomainExceptionType exceptionType, String message, Throwable cause) {
        super(message, cause);
        this.exceptionType = exceptionType;
    }

    /**
     * 예외 타입으로 예외 생성
     */
    public static GameDomainException of(GameDomainExceptionType type) {
        return new GameDomainException(type, type.getMessage());
    }

    /**
     * 예외 타입과 커스텀 메시지로 예외 생성
     */
    public static GameDomainException of(GameDomainExceptionType type, String customMessage) {
        return new GameDomainException(type, customMessage);
    }

    /**
     * 예외 타입과 원인으로 예외 생성
     */
    public static GameDomainException of(GameDomainExceptionType type, Throwable cause) {
        return new GameDomainException(type, type.getMessage(), cause);
    }

    /**
     * 예외 타입, 커스텀 메시지, 원인으로 예외 생성
     */
    public static GameDomainException of(GameDomainExceptionType type, String customMessage, Throwable cause) {
        return new GameDomainException(type, customMessage, cause);
    }

    /**
     * 에러 코드 반환
     */
    public String getErrorCode() {
        return exceptionType.getErrorCode();
    }
}
