package com.pigeon3.ssamantle.application.user.port.in;

/**
 * 유저 정보 수정 UseCase 인터페이스 (인바운드 포트)
 */
public interface UpdateUserUseCase {
    UpdateUserResponse execute(UpdateUserCommand command);
}
