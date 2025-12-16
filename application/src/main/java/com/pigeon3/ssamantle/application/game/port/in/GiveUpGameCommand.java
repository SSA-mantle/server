package com.pigeon3.ssamantle.application.game.port.in;

public record GiveUpGameCommand(
    Long userId
) {
    public GiveUpGameCommand {
        if (userId == null) {
            throw new IllegalArgumentException("userId는 필수입니다.");
        }
    }
}
