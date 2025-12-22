package com.pigeon3.ssamantle.adapter.in.http.game.dto;

import com.pigeon3.ssamantle.application.game.port.in.GetGameStatusResponse;
import lombok.Builder;

@Builder
public record GetGameStatusResponseDto(
    String status,
    Integer failCount
) {
    public static GetGameStatusResponseDto from(GetGameStatusResponse response) {
        return GetGameStatusResponseDto.builder()
            .status(response.status())
            .failCount(response.failCount())
            .build();
    }
}
