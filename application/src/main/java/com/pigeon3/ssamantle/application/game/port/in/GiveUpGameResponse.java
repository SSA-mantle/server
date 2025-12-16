package com.pigeon3.ssamantle.application.game.port.in;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record GiveUpGameResponse(
    String message,
    String answer,              // 정답 단어
    int failCount,              // 실패 횟수
    LocalDateTime giveUpAt      // 포기 시간
) {
    public static GiveUpGameResponse of(String answer, int failCount, LocalDateTime giveUpAt) {
        return GiveUpGameResponse.builder()
            .message("포기하셨습니다. 정답은 '" + answer + "' 입니다.")
            .answer(answer)
            .failCount(failCount)
            .giveUpAt(giveUpAt)
            .build();
    }
}
