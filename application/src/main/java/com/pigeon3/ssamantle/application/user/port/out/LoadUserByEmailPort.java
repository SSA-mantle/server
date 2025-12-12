package com.pigeon3.ssamantle.application.user.port.out;

import com.pigeon3.ssamantle.domain.model.user.User;
import com.pigeon3.ssamantle.domain.model.user.vo.Email;

import java.util.Optional;

/**
 * 이메일로 사용자 조회 포트 (아웃바운드 포트)
 */
public interface LoadUserByEmailPort {
    Optional<User> loadByEmail(Email email);
}
