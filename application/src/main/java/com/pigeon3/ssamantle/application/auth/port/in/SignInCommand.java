package com.pigeon3.ssamantle.application.auth.port.in;

/**
 * 로그인 커맨드
 */
public record SignInCommand(
        String email
) {
}
