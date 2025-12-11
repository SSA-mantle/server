package com.pigeon3.ssamantle.adapter.in.http.user.dto;

import com.pigeon3.ssamantle.application.user.port.in.SignUpCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 회원가입 HTTP 요청 DTO
 */
public record SignUpRequest(
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    String email,

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 15, message = "비밀번호는 8자 이상 15자 이하여야 합니다.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]+$",
        message = "비밀번호는 알파벳 대소문자, 숫자, 특수문자(@$!%*?&#)를 모두 포함해야 합니다."
    )
    String password,

    @NotBlank(message = "닉네임은 필수입니다.")
    String nickname
) {
    /**
     * Command로 변환 (암호화된 비밀번호 사용)
     */
    public SignUpCommand toCommand(String encodedPassword) {
        return new SignUpCommand(email, encodedPassword, nickname);
    }
}
