package com.pigeon3.ssamantle.adapter.out.rdb.record.mapper;

import com.pigeon3.ssamantle.adapter.out.rdb.record.entity.RecordEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecordMapper {

    /**
     * 기록 저장
     */
    void insert(RecordEntity entity);

    /**
     * 기록 업데이트
     */
    void update(RecordEntity entity);

    /**
     * ID로 기록 조회
     */
    RecordEntity findById(Long id);

    /**
     * 사용자 ID와 문제 ID로 기록 조회
     */
    RecordEntity findByUserIdAndProblemId(Long userId, Long problemId);
}
