package com.pigeon3.ssamantle.application.user.port.out;

import com.pigeon3.ssamantle.domain.model.user.User;

/**
 * 사용자 수정 포트 (아웃바운드 포트)
 */
public interface UpdateUserPort {
    User update(User user);
}