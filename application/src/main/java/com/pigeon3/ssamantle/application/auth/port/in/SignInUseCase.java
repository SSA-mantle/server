package com.pigeon3.ssamantle.application.auth.port.in;

/**
 * 로그인 UseCase (인바운드 포트)
 */
public interface SignInUseCase {
    SignInResponse signIn(SignInCommand command);
}
