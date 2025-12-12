package com.pigeon3.ssamantle.application.auth.service;

import com.pigeon3.ssamantle.application.auth.port.in.SignInCommand;
import com.pigeon3.ssamantle.application.auth.port.in.SignInResponse;
import com.pigeon3.ssamantle.application.auth.port.in.SignInUseCase;
import com.pigeon3.ssamantle.application.common.exception.ApplicationException;
import com.pigeon3.ssamantle.application.common.exception.ExceptionType;
import com.pigeon3.ssamantle.application.user.port.out.LoadUserByEmailPort;
import com.pigeon3.ssamantle.domain.model.user.User;
import com.pigeon3.ssamantle.domain.model.user.vo.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 로그인 UseCase 구현체
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignInService implements SignInUseCase {

    private final LoadUserByEmailPort loadUserByEmailPort;

    @Override
    public SignInResponse signIn(SignInCommand command) {
        Email email = Email.of(command.email());

        User user = loadUserByEmailPort.loadByEmail(email)
                .orElseThrow(() -> ApplicationException.of(ExceptionType.INVALID_CREDENTIALS));

        if (user.isDelete()) {
            throw ApplicationException.of(ExceptionType.USER_DELETED);
        }

        return new SignInResponse(
                user.getId(),
                user.getEmail().getValue(),
                user.getNickname().getValue(),
                user.getRole().name(),
                user.getPassword().getValue()  // 비밀번호 해시 반환 (HTTP 계층에서 검증용)
        );
    }
}
