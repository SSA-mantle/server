package com.pigeon3.ssamantle.application.user.port.out;

import com.pigeon3.ssamantle.domain.model.user.vo.Email;

/**
 * 이메일 중복 확인 포트 (아웃바운드)
 */
public interface CheckEmailDuplicationPort {
    boolean existsByEmail(Email email);
}
