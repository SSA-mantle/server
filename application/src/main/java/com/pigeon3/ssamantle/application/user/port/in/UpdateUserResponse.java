package com.pigeon3.ssamantle.application.user.port.in;

import com.pigeon3.ssamantle.domain.model.user.User;

/**
 * 유저 정보 수정 응답 (출력 DTO)
 */
public record UpdateUserResponse(
    Long userId,
    String email,
    String nickname,
    String message
) {
    public static UpdateUserResponse from(User user) {
        return new UpdateUserResponse(
            user.getId(),
            user.getEmail().getValue(),
            user.getNickname().getValue(),
            "유저 정보가 성공적으로 수정되었습니다."
        );
    }
}