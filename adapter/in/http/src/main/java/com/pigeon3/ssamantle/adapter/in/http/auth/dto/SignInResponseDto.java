package com.pigeon3.ssamantle.adapter.in.http.auth.dto;

import com.pigeon3.ssamantle.application.auth.port.in.SignInResponse;

/**
 * 로그인 응답 DTO
 */
public record SignInResponseDto(
        Long userId,
        String email,
        String nickname,
        String role,
        String accessToken,
        String refreshToken
) {
    public static SignInResponseDto from(SignInResponse response, String accessToken, String refreshToken) {
        return new SignInResponseDto(
                response.userId(),
                response.email(),
                response.nickname(),
                response.role(),
                accessToken,
                refreshToken
        );
    }
}
