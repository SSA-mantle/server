package com.pigeon3.ssamantle.application.user.port.out;

import com.pigeon3.ssamantle.domain.model.user.User;

/**
 * 사용자 저장 포트 (아웃바운드)
 */
public interface SaveUserPort {
    User save(User user);
}
