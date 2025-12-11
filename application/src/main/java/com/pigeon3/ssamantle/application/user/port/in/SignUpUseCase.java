package com.pigeon3.ssamantle.application.user.port.in;

/**
 * 회원가입 UseCase 인터페이스 (인바운드 포트)
 */
public interface SignUpUseCase {
    SignUpResponse execute(SignUpCommand command);
}
