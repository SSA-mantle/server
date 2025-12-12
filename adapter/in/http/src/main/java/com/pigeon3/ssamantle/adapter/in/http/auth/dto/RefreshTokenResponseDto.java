package com.pigeon3.ssamantle.adapter.in.http.auth.dto;

/**
 * 토큰 갱신 응답 DTO
 */
public record RefreshTokenResponseDto(
        String accessToken,
        String refreshToken
) {
}
