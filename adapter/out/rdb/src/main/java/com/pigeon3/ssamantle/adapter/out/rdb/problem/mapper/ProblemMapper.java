package com.pigeon3.ssamantle.adapter.out.rdb.problem.mapper;

import com.pigeon3.ssamantle.adapter.out.rdb.problem.entity.ProblemEntity;
import org.apache.ibatis.annotations.Mapper;
import java.time.LocalDate;

@Mapper
public interface ProblemMapper {

    /**
     * 날짜로 문제 조회
     */
    ProblemEntity findByDate(LocalDate date);

    /**
     * 문제 저장
     */
    void insert(ProblemEntity entity);
}
