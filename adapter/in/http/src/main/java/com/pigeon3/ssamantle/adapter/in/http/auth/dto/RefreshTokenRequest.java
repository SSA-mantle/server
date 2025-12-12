package com.pigeon3.ssamantle.adapter.in.http.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 토큰 갱신 요청 DTO
 */
public record RefreshTokenRequest(
        @NotBlank(message = "Refresh token은 필수입니다.")
        String refreshToken
) {
}
