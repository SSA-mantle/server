package com.pigeon3.ssamantle.adapter.out.rdb.problem.entity;

import com.pigeon3.ssamantle.domain.model.problem.Problem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Problem 영속성 엔티티 (MyBatis용)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemEntity {
    private Long id;
    private String answer;
    private LocalDate date;

    /**
     * 영속성 엔티티 → 도메인 모델 변환
     */
    public Problem toDomain() {
        return Problem.reconstruct(
                this.id,
                this.answer,
                this.date
        );
    }

    /**
     * 도메인 모델 → 영속성 엔티티 변환
     */
    public static ProblemEntity fromDomain(Problem problem) {
        return ProblemEntity.builder()
                .id(problem.getId())
                .answer(problem.getAnswer())
                .date(problem.getDate())
                .build();
    }
}
