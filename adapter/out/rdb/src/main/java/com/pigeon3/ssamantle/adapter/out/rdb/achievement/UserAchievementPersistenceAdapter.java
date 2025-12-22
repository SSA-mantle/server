package com.pigeon3.ssamantle.adapter.out.rdb.achievement;

import com.pigeon3.ssamantle.adapter.out.rdb.achievement.entity.UserAchievementEntity;
import com.pigeon3.ssamantle.adapter.out.rdb.achievement.mapper.UserAchievementMapper;
import com.pigeon3.ssamantle.application.achievement.port.out.LoadUserAchievementsPort;
import com.pigeon3.ssamantle.application.achievement.port.out.SaveUserAchievementPort;
import com.pigeon3.ssamantle.domain.model.achievement.UserAchievement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 사용자 업적 영속성 어댑터
 */
@Component
@RequiredArgsConstructor
public class UserAchievementPersistenceAdapter implements
        LoadUserAchievementsPort,
        SaveUserAchievementPort {

    private final UserAchievementMapper userAchievementMapper;

    @Override
    public List<UserAchievement> loadByUserId(Long userId) {
        List<UserAchievementEntity> entities = userAchievementMapper.findByUserId(userId);
        return entities.stream()
                .map(UserAchievementEntity::toDomain)
                .toList();
    }

    @Override
    public UserAchievement save(UserAchievement achievement) {
        UserAchievementEntity entity = UserAchievementEntity.fromDomain(achievement);
        userAchievementMapper.insert(entity);
        return entity.toDomain();
    }
}
