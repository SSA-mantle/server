package com.pigeon3.ssamantle.application.user.service;

import com.pigeon3.ssamantle.application.common.exception.ApplicationException;
import com.pigeon3.ssamantle.application.common.exception.ExceptionType;
import com.pigeon3.ssamantle.application.user.port.in.SignUpCommand;
import com.pigeon3.ssamantle.application.user.port.in.SignUpResponse;
import com.pigeon3.ssamantle.application.user.port.in.SignUpUseCase;
import com.pigeon3.ssamantle.application.user.port.out.CheckEmailDuplicationPort;
import com.pigeon3.ssamantle.application.user.port.out.CheckNicknameDuplicationPort;
import com.pigeon3.ssamantle.application.user.port.out.SaveUserPort;
import com.pigeon3.ssamantle.domain.model.user.User;
import com.pigeon3.ssamantle.domain.model.user.exception.UserDomainException;
import com.pigeon3.ssamantle.domain.model.user.vo.Email;
import com.pigeon3.ssamantle.domain.model.user.vo.Nickname;
import com.pigeon3.ssamantle.domain.model.user.vo.Password;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * 회원가입 UseCase 구현체
 */
@Service
@RequiredArgsConstructor
public class SignUpService implements SignUpUseCase {

    private final SaveUserPort saveUserPort;
    private final CheckEmailDuplicationPort checkEmailDuplicationPort;
    private final CheckNicknameDuplicationPort checkNicknameDuplicationPort;

    @Transactional
    @Override
    public SignUpResponse execute(SignUpCommand command) {
        // 1. 값 객체 생성 (도메인 예외를 application 예외로 변환)
        Email email = createEmail(command.email());
        Password password = Password.of(command.password()); // 이미 암호화된 비밀번호
        Nickname nickname = createNickname(command.nickname());

        // 2. 이메일 중복 확인
        if (checkEmailDuplicationPort.existsByEmail(email)) {
            throw ApplicationException.of(ExceptionType.DUPLICATE_EMAIL);
        }

        // 3. 닉네임 중복 확인
        if (checkNicknameDuplicationPort.existsByNickname(nickname)) {
            throw ApplicationException.of(ExceptionType.DUPLICATE_NICKNAME);
        }

        // 4. 도메인 모델 생성
        User user = User.create(email, password, nickname);

        // 5. 저장
        User savedUser = saveUserPort.save(user);

        // 6. 응답 반환
        return SignUpResponse.from(savedUser);
    }

    /**
     * Email 값 객체 생성 (도메인 예외를 application 예외로 변환)
     */
    private Email createEmail(String email) {
        try {
            return Email.of(email);
        } catch (UserDomainException e) {
            throw ApplicationException.of(ExceptionType.INVALID_EMAIL, e.getMessage());
        }
    }

    /**
     * Nickname 값 객체 생성 (도메인 예외를 application 예외로 변환)
     */
    private Nickname createNickname(String nickname) {
        try {
            return Nickname.of(nickname);
        } catch (UserDomainException e) {
            throw ApplicationException.of(ExceptionType.INVALID_NICKNAME, e.getMessage());
        }
    }
}
