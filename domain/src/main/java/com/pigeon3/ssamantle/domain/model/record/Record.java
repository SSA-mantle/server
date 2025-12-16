package com.pigeon3.ssamantle.domain.model.record;

import com.pigeon3.ssamantle.domain.model.record.exception.RecordDomainException;
import com.pigeon3.ssamantle.domain.model.record.exception.RecordDomainExceptionType;

import java.time.LocalDateTime;
import java.util.Objects;

import lombok.Getter;

/**
 * 기록 도메인 모델
 * 사용자가 문제를 풀었을 때의 기록
 */
@Getter
public class Record {
    private final Long id;
    private final Long userId;
    private final Long problemId;
    private int failCount;
    private LocalDateTime solvedAt;
    private LocalDateTime giveUpAt;
    private final LocalDateTime createdAt;

    private Record(Long id, Long userId, Long problemId, int failCount,
                   LocalDateTime solvedAt, LocalDateTime giveUpAt, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.problemId = problemId;
        this.failCount = failCount;
        this.solvedAt = solvedAt;
        this.giveUpAt = giveUpAt;
        this.createdAt = createdAt;
    }

    /**
     * 새로운 기록 생성 (문제 시작)
     */
    public static Record create(Long userId, Long problemId) {
        return new Record(
                null,
                userId,
                problemId,
                0,
                null,
                null,
                LocalDateTime.now()
        );
    }

    /**
     * 기존 기록 재구성 (영속성 계층에서 사용)
     */
    public static Record reconstruct(Long id, Long userId, Long problemId, int failCount,
                                      LocalDateTime solvedAt, LocalDateTime giveUpAt, LocalDateTime createdAt) {
        return new Record(id, userId, problemId, failCount, solvedAt, giveUpAt, createdAt);
    }

    /**
     * 오답 시 실패 횟수 증가
     */
    public void incrementFailCount() {
        this.failCount++;
    }

    /**
     * 실패 횟수 업데이트 (프론트엔드에서 관리하는 경우)
     */
    public void updateFailCount(int failCount) {
        if (failCount < 0) {
            throw new IllegalArgumentException("실패 횟수는 0 이상이어야 합니다: " + failCount);
        }
        this.failCount = failCount;
    }

    /**
     * 문제 해결 완료
     */
    public void solve() {
        this.solvedAt = LocalDateTime.now();
    }

	public boolean isSolved() {
        return solvedAt != null;
    }

    /**
     * 문제 포기
     */
    public void giveUp() {
        if (this.solvedAt != null) {
            throw RecordDomainException.of(RecordDomainExceptionType.ALREADY_SOLVED);
        }
        if (this.giveUpAt != null) {
            throw RecordDomainException.of(RecordDomainExceptionType.ALREADY_GAVE_UP);
        }
        this.giveUpAt = LocalDateTime.now();
    }

    /**
     * 포기 여부 확인
     */
    public boolean isGaveUp() {
        return giveUpAt != null;
    }

    /**
     * 진행 가능한 상태인지 확인 (풀지도 않고 포기하지도 않음)
     */
    public boolean isInProgress() {
        return solvedAt == null && giveUpAt == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return Objects.equals(id, record.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", userId=" + userId +
                ", problemId=" + problemId +
                ", failCount=" + failCount +
                ", solvedAt=" + solvedAt +
                '}';
    }
}
