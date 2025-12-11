package com.pigeon3.ssamantle.application.user.port.in;

/**
 * 회원가입 명령 (입력 DTO)
 * 이미 암호화된 비밀번호를 받음
 */
public record SignUpCommand(
    String email,
    String password, // 이미 암호화된 비밀번호
    String nickname
) {
    public SignUpCommand {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("닉네임은 필수입니다.");
        }
    }
}
