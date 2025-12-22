package com.pigeon3.ssamantle.application.game.port.in;

public interface GetAnswerHistoryUseCase {
    GetAnswerHistoryResponse execute(GetAnswerHistoryCommand command);
}
