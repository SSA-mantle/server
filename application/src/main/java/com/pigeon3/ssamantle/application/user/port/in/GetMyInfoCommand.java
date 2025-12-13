package com.pigeon3.ssamantle.application.user.port.in;

public record GetMyInfoCommand(Long userId) {
    public GetMyInfoCommand {
        if (userId == null) {
            throw new IllegalArgumentException("userId는 필수입니다.");
        }
    }
}
