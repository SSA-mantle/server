package com.pigeon3.ssamantle.application.user.port.in;

import com.pigeon3.ssamantle.domain.model.user.User;

/**
 * 회원가입 응답 (출력 DTO)
 */
public record SignUpResponse(
    Long userId,
    String email,
    String nickname,
    String role
) {
    public static SignUpResponse from(User user) {
        return new SignUpResponse(
            user.getId(),
            user.getEmail().getValue(),
            user.getNickname().getValue(),
            user.getRole().name()
        );
    }
}
