package com.pigeon3.ssamantle.application.game.port.in;

public interface SubmitGuessUseCase {
    SubmitGuessResponse execute(SubmitGuessCommand command);
}
