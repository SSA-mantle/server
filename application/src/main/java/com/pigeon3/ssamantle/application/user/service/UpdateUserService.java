package com.pigeon3.ssamantle.application.user.service;

import com.pigeon3.ssamantle.application.common.exception.ApplicationException;
import com.pigeon3.ssamantle.application.common.exception.ExceptionType;
import com.pigeon3.ssamantle.application.user.port.in.UpdateUserCommand;
import com.pigeon3.ssamantle.application.user.port.in.UpdateUserResponse;
import com.pigeon3.ssamantle.application.user.port.in.UpdateUserUseCase;
import com.pigeon3.ssamantle.application.user.port.out.CheckNicknameDuplicationPort;
import com.pigeon3.ssamantle.application.user.port.out.LoadUserByIdPort;
import com.pigeon3.ssamantle.application.user.port.out.UpdateUserPort;
import com.pigeon3.ssamantle.domain.model.user.User;
import com.pigeon3.ssamantle.domain.model.user.exception.UserDomainException;
import com.pigeon3.ssamantle.domain.model.user.vo.Nickname;
import com.pigeon3.ssamantle.domain.model.user.vo.Password;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * 유저 정보 수정 UseCase 구현체
 */
@Service
@RequiredArgsConstructor
public class UpdateUserService implements UpdateUserUseCase {

    private final LoadUserByIdPort loadUserByIdPort;
    private final UpdateUserPort updateUserPort;
    private final CheckNicknameDuplicationPort checkNicknameDuplicationPort;

    @Transactional
    @Override
    public UpdateUserResponse execute(UpdateUserCommand command) {
        // 1. 사용자 조회
        User user = loadUserByIdPort.loadById(command.userId())
                .orElseThrow(() -> ApplicationException.of(ExceptionType.USER_NOT_FOUND));

        // 2. 비밀번호 수정
        if (command.hasPassword()) {
            Password newPassword = Password.of(command.password()); // 이미 암호화된 비밀번호
            user.changePassword(newPassword);
        }

        // 3. 닉네임 수정
        if (command.hasNickname()) {
            Nickname newNickname = createNickname(command.nickname());

            // 현재 닉네임과 다를 경우에만 중복 체크
            if (!user.getNickname().equals(newNickname)) {
                if (checkNicknameDuplicationPort.existsByNickname(newNickname)) {
                    throw ApplicationException.of(ExceptionType.DUPLICATE_NICKNAME);
                }
                user.changeNickname(newNickname);
            }
        }

        // 4. 저장
        User updatedUser = updateUserPort.update(user);

        // 5. 응답 반환
        return UpdateUserResponse.from(updatedUser);
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
