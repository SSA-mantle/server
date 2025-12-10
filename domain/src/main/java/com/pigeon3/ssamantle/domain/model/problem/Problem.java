package com.pigeon3.ssamantle.domain.model.problem;

import com.pigeon3.ssamantle.domain.model.problem.exception.ProblemDomainException;

import java.time.LocalDate;
import java.util.Objects;

import lombok.Getter;

/**
 * 문제 도메인 모델
 * 매일의 정답 단어를 나타냄
 */
@Getter
public class Problem {
    private final Long id;
    private final String answer;
    private final LocalDate date;

    private Problem(Long id, String answer, LocalDate date) {
        this.id = id;
        this.answer = answer;
        this.date = date;
    }

    /**
     * 새로운 문제 생성
     */
    public static Problem create(String answer, LocalDate date) {
        validateAnswer(answer);
        return new Problem(null, answer, date);
    }

    /**
     * 기존 문제 재구성 (영속성 계층에서 사용)
     */
    public static Problem reconstruct(Long id, String answer, LocalDate date) {
        return new Problem(id, answer, date);
    }

    private static void validateAnswer(String answer) {
        if (answer == null || answer.isBlank()) {
            throw new ProblemDomainException("정답 단어는 필수입니다.");
        }
        if (answer.length() > 255) {
            throw new ProblemDomainException("정답 단어는 255자를 초과할 수 없습니다: " + answer.length() + "자");
        }
    }

    /**
     * 제출한 답이 정답인지 확인
     */
    public boolean isCorrectAnswer(String submittedAnswer) {
        if (submittedAnswer == null) {
            return false;
        }
        return answer.equals(submittedAnswer);
    }

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Problem problem = (Problem) o;
        return Objects.equals(id, problem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Problem{" +
                "id=" + id +
                ", date=" + date +
                '}';
    }
}
