package com.pigeon3.ssamantle.adapter.out.rdb.user.mapper;

import com.pigeon3.ssamantle.adapter.out.rdb.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * User MyBatis Mapper
 */
@Mapper
public interface UserMapper {

    /**
     * 사용자 저장
     */
    void insert(UserEntity entity);

    /**
     * 이메일로 사용자 존재 여부 확인
     */
    boolean existsByEmail(String email);

    /**
     * 닉네임으로 사용자 존재 여부 확인
     */
    boolean existsByNickname(String nickname);

    /**
     * ID로 사용자 조회 (저장 후 조회용)
     */
    UserEntity findById(Long id);

    /**
     * 이메일로 사용자 조회
     */
    UserEntity findByEmail(String email);

    /**
     * 사용자 정보 업데이트
     */
    void update(UserEntity entity);

    /**
     * 여러 사용자를 ID로 조회
     */
    List<UserEntity> findByIds(@Param("ids") List<Long> ids);

    /**
     * 일자 변경 시: 모든 사용자 todaySolve 초기화 + (미해결자는 nowCont=0)
     */
    void resetTodaySolveForAll();
}
