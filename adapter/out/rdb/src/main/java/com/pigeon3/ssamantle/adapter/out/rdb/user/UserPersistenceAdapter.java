package com.pigeon3.ssamantle.adapter.out.rdb.user;

import com.pigeon3.ssamantle.adapter.out.rdb.user.entity.UserEntity;
import com.pigeon3.ssamantle.adapter.out.rdb.user.mapper.UserMapper;
import com.pigeon3.ssamantle.application.user.port.out.CheckEmailDuplicationPort;
import com.pigeon3.ssamantle.application.user.port.out.CheckNicknameDuplicationPort;
import com.pigeon3.ssamantle.application.user.port.out.LoadUserByEmailPort;
import com.pigeon3.ssamantle.application.user.port.out.SaveUserPort;
import com.pigeon3.ssamantle.domain.model.user.User;
import com.pigeon3.ssamantle.domain.model.user.vo.Email;
import com.pigeon3.ssamantle.domain.model.user.vo.Nickname;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * User 영속성 어댑터
 * 아웃바운드 포트 구현체
 */
@Component
public class UserPersistenceAdapter implements
        SaveUserPort,
        CheckEmailDuplicationPort,
        CheckNicknameDuplicationPort,
        LoadUserByEmailPort {

    private final UserMapper userMapper;

    public UserPersistenceAdapter(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        // 1. 도메인 모델 → 영속성 엔티티 변환
        // 주의: 비밀번호는 이미 암호화된 상태로 전달됨
        UserEntity entity = UserEntity.builder()
                .email(user.getEmail().getValue())
                .password(user.getPassword().getValue()) // 이미 암호화된 비밀번호
                .role(user.getRole().name())
                .nickname(user.getNickname().getValue())
                .todaySolve(user.isTodaySolve())
                .longestCont(user.getLongestCont())
                .nowCont(user.getNowCont())
                .isDelete(user.isDelete())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

        // 2. DB 저장
        userMapper.insert(entity);

        // 3. 저장된 엔티티 조회 (ID 포함)
        UserEntity savedEntity = userMapper.findById(entity.getId());

        // 4. 영속성 엔티티 → 도메인 모델 변환
        return savedEntity.toDomain();
    }

    @Override
    public boolean existsByEmail(Email email) {
        return userMapper.existsByEmail(email.getValue());
    }

    @Override
    public boolean existsByNickname(Nickname nickname) {
        return userMapper.existsByNickname(nickname.getValue());
    }

    @Override
    public Optional<User> loadByEmail(Email email) {
        UserEntity entity = userMapper.findByEmail(email.getValue());
        return Optional.ofNullable(entity)
                .map(UserEntity::toDomain);
    }
}
