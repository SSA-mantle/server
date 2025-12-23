package com.pigeon3.ssamantle.adapter.in.http.user.dto;

import com.pigeon3.ssamantle.application.user.port.in.UpdateUserCommand;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 유저 정보 수정 HTTP 요청 DTO
 */
public record UpdateUserRequest(
    @Size(min = 8, max = 15, message = "비밀번호는 8자 이상 15자 이하여야 합니다.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]+$",
        message = "비밀번호는 알파벳 소문자, 숫자, 특수문자(@$!%*?&#)를 모두 포함해야 합니다."
    )
    String password,  // 선택적

    @Size(min = 3, max = 20, message = "닉네임은 3자 이상 20자 이하여야 합니다.")
    String nickname   // 선택적
) {
    /**
     * Command로 변환 (암호화된 비밀번호 사용)
     */
    public UpdateUserCommand toCommand(Long userId, String encodedPassword) {
        return new UpdateUserCommand(userId, encodedPassword, nickname);
    }
}
