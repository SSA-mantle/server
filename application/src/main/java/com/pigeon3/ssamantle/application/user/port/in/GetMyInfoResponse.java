package com.pigeon3.ssamantle.application.user.port.in;

import com.pigeon3.ssamantle.domain.model.user.User;

public record GetMyInfoResponse(
    Long userId,
    String email,
    String nickname,
    String role
) {
    public static GetMyInfoResponse from(User user) {
        return new GetMyInfoResponse(
            user.getId(),
            user.getEmail().getValue(),
            user.getNickname().getValue(),
            user.getRole().name()
        );
    }
}
