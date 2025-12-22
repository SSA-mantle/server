package com.pigeon3.ssamantle.adapter.out.rdb.achievement.mapper;

import com.pigeon3.ssamantle.adapter.out.rdb.achievement.entity.UserAchievementEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 사용자 업적 MyBatis 매퍼
 */
@Mapper
public interface UserAchievementMapper {

    /**
     * 업적 저장
     */
    void insert(UserAchievementEntity entity);

    /**
     * 사용자 ID로 모든 업적 조회
     */
    List<UserAchievementEntity> findByUserId(@Param("userId") Long userId);

    /**
     * 사용자 ID와 업적 타입으로 존재 여부 확인
     */
    boolean existsByUserIdAndType(@Param("userId") Long userId, @Param("achievementType") String achievementType);
}
