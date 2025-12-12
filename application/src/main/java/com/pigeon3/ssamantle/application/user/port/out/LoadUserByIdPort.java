package com.pigeon3.ssamantle.application.user.port.out;

import com.pigeon3.ssamantle.domain.model.user.User;

import java.util.Optional;

/**
 * ID로 사용자 조회 포트 (아웃바운드 포트)
 */
public interface LoadUserByIdPort {
    Optional<User> loadById(Long userId);
}
