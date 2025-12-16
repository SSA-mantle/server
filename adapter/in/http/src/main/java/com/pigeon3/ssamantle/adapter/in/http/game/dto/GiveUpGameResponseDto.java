package com.pigeon3.ssamantle.adapter.in.http.game.dto;

import com.pigeon3.ssamantle.application.game.port.in.GiveUpGameResponse;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record GiveUpGameResponseDto(
    String message,
    String answer,
    int failCount,
    LocalDateTime giveUpAt
) {
    public static GiveUpGameResponseDto from(GiveUpGameResponse response) {
        return GiveUpGameResponseDto.builder()
            .message(response.message())
            .answer(response.answer())
            .failCount(response.failCount())
            .giveUpAt(response.giveUpAt())
            .build();
    }
}
