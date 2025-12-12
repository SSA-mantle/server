package com.pigeon3.ssamantle.adapter.in.http.user.dto;

import com.pigeon3.ssamantle.application.user.port.in.UpdateUserResponse;

/**
 * 유저 정보 수정 HTTP 응답 DTO
 */
public record UpdateUserResponseDto(
    Long userId,
    String email,
    String nickname,
    String message
) {
    public static UpdateUserResponseDto from(UpdateUserResponse response) {
        return new UpdateUserResponseDto(
            response.userId(),
            response.email(),
            response.nickname(),
            response.message()
        );
    }
}
