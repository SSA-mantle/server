package com.pigeon3.ssamantle.application.auth.port.in;

/**
 * 로그인 응답
 */
public record SignInResponse(
        Long userId,
        String email,
        String nickname,
        String role,
        String passwordHash  // HTTP 계층에서 검증용
) {
}
