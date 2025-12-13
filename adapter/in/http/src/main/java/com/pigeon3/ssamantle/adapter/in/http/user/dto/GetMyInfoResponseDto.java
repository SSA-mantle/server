package com.pigeon3.ssamantle.adapter.in.http.user.dto;

import com.pigeon3.ssamantle.application.user.port.in.GetMyInfoResponse;

public record GetMyInfoResponseDto(
    Long userId,
    String email,
    String nickname,
    String role
) {
    public static GetMyInfoResponseDto from(GetMyInfoResponse response) {
        return new GetMyInfoResponseDto(
            response.userId(),
            response.email(),
            response.nickname(),
            response.role()
        );
    }
}
