package com.pigeon3.ssamantle.application.game.port.in;

public interface GiveUpGameUseCase {
    GiveUpGameResponse execute(GiveUpGameCommand command);
}
