package com.pigeon3.ssamantle.adapter.out.rdb.record.entity;

import com.pigeon3.ssamantle.domain.model.record.Record;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Record 영속성 엔티티 (MyBatis용)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordEntity {
    private Long id;
    private Long userId;
    private Long problemId;
    private Integer failCount;
    private LocalDateTime solvedAt;
    private LocalDateTime giveUpAt;
    private LocalDateTime createdAt;

    /**
     * 영속성 엔티티 → 도메인 모델 변환
     */
    public Record toDomain() {
        return Record.reconstruct(
                this.id,
                this.userId,
                this.problemId,
                this.failCount,
                this.solvedAt,
                this.giveUpAt,
                this.createdAt
        );
    }
}
