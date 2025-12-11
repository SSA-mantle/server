package com.pigeon3.ssamantle.application.user.port.out;

import com.pigeon3.ssamantle.domain.model.user.vo.Nickname;

/**
 * 닉네임 중복 확인 포트 (아웃바운드)
 */
public interface CheckNicknameDuplicationPort {
    boolean existsByNickname(Nickname nickname);
}
