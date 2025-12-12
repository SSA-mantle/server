package com.pigeon3.ssamantle.application.user.port.in;

import com.pigeon3.ssamantle.application.common.exception.ApplicationException;
import com.pigeon3.ssamantle.application.common.exception.ExceptionType;

/**
 * 유저 정보 수정 명령 (입력 DTO)
 */
public record UpdateUserCommand(
    Long userId,
    String password,  // 선택적 (암호화된 비밀번호)
    String nickname   // 선택적
) {
    public UpdateUserCommand {
        if (userId == null) {
            throw ApplicationException.of(ExceptionType.BAD_REQUEST, "유저 ID는 필수입니다.");
        }

        // 최소한 하나의 수정 항목은 있어야 함
        if ((password == null || password.isBlank()) &&
            (nickname == null || nickname.isBlank())) {
            throw ApplicationException.of(ExceptionType.NO_UPDATE_FIELD);
        }
    }

    public boolean hasPassword() {
        return password != null && !password.isBlank();
    }

    public boolean hasNickname() {
        return nickname != null && !nickname.isBlank();
    }
}
