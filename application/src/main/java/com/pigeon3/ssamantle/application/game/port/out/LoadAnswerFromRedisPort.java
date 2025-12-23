package com.pigeon3.ssamantle.application.game.port.out;

import java.time.LocalDate;
import java.util.Optional;

public interface LoadAnswerFromRedisPort {
    /**
     * Redis에서 정답 단어 조회
     * Key: "ssamantle:{date}:answer" (STRING)
     */
    Optional<String> loadAnswer(LocalDate date);

    /**
     * Redis에서 정답 단어 설명 조회
     * Key: "ssamantle:{date}:answer_desc" (STRING)
     */
    Optional<String> loadAnswerDescription(LocalDate date);
}
