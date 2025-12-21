package com.pigeon3.ssamantle.adapter.out.rdb.user;

import com.pigeon3.ssamantle.adapter.out.rdb.user.entity.UserEntity;
import com.pigeon3.ssamantle.adapter.out.rdb.user.mapper.UserMapper;
import com.pigeon3.ssamantle.application.leaderboard.port.out.LoadUsersByIdsPort;
import com.pigeon3.ssamantle.application.user.port.out.*;
import com.pigeon3.ssamantle.domain.model.user.User;
import com.pigeon3.ssamantle.domain.model.user.vo.Email;
import com.pigeon3.ssamantle.domain.model.user.vo.Nickname;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User 영속성 어댑터
 * 아웃바운드 포트 구현체
 */
@Component
public class UserPersistenceAdapter implements
        SaveUserPort,
        UpdateUserPort,
        LoadUserByIdPort,
        LoadUserByEmailPort,
        CheckEmailDuplicationPort,
        CheckNicknameDuplicationPort,
        LoadUsersByIdsPort,
        ResetTodaySolveForAllUsersPort {

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
                .bestRank(user.getBestRank())
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

    @Override
    public Optional<User> loadById(Long userId) {
        UserEntity entity = userMapper.findById(userId);
        return Optional.ofNullable(entity)
                .map(UserEntity::toDomain);
    }

    @Override
    public User update(User user) {
        // 1. 도메인 모델 → 영속성 엔티티 변환
        UserEntity entity = UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail().getValue())
                .password(user.getPassword().getValue())
                .role(user.getRole().name())
                .nickname(user.getNickname().getValue())
                .todaySolve(user.isTodaySolve())
                .longestCont(user.getLongestCont())
                .nowCont(user.getNowCont())
                .bestRank(user.getBestRank())
                .isDelete(user.isDelete())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .deletedAt(user.getDeletedAt())
                .build();

        // 2. DB 업데이트
        userMapper.update(entity);

        // 3. 업데이트된 엔티티 조회
        UserEntity updatedEntity = userMapper.findById(entity.getId());

        // 4. 영속성 엔티티 → 도메인 모델 변환
        return updatedEntity.toDomain();
    }

    @Override
    public Map<Long, User> loadByIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Map.of();
        }

        List<UserEntity> entities = userMapper.findByIds(userIds);
        return entities.stream()
                .map(UserEntity::toDomain)
                .collect(Collectors.toMap(User::getId, user -> user));
    }

    @Override
    public void resetTodaySolveForAll() {
        userMapper.resetTodaySolveForAll();
    }
}
