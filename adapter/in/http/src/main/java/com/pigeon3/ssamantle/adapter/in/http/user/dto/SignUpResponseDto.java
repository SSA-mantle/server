package com.pigeon3.ssamantle.adapter.in.http.user.dto;

import com.pigeon3.ssamantle.application.user.port.in.SignUpResponse;

/**
 * 회원가입 HTTP 응답 DTO
 */
public record SignUpResponseDto(
    Long userId,
    String email,
    String nickname,
    String role
) {
    public static SignUpResponseDto from(SignUpResponse response) {
        return new SignUpResponseDto(
            response.userId(),
            response.email(),
            response.nickname(),
            response.role()
        );
    }
}
