package com.pigeon3.ssamantle.domain.model.game.vo;

import lombok.Getter;
import java.util.Objects;

/**
 * 단어 유사도 값 객체
 * 유사도는 0.0 ~ 100.0 사이의 값
 * rank가 -1인 경우 순위 없음을 의미 (1000위 초과)
 */
@Getter
public class WordSimilarity {
    private final String word;
    private final double similarity;
    private final int rank;

    private WordSimilarity(String word, double similarity, int rank) {
        this.word = word;
        this.similarity = similarity;
        this.rank = rank;
    }

    public static WordSimilarity of(String word, double similarity, int rank) {
        validateWord(word);
        validateSimilarity(similarity);
        validateRank(rank);
        return new WordSimilarity(word, similarity, rank);
    }

    private static void validateWord(String word) {
        if (word == null || word.isBlank()) {
            throw new IllegalArgumentException("단어는 필수입니다.");
        }
    }

    private static void validateSimilarity(double similarity) {
        if (similarity < 0.0 || similarity > 100.0) {
            throw new IllegalArgumentException("유사도는 0.0 ~ 100.0 사이여야 합니다: " + similarity);
        }
    }

    private static void validateRank(int rank) {
        if (rank < -1) {
            throw new IllegalArgumentException("순위는 -1 이상이어야 합니다 (-1: 순위 없음, 0: 정답, 1~: 실제 순위): " + rank);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordSimilarity that = (WordSimilarity) o;
        return Objects.equals(word, that.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }

    @Override
    public String toString() {
        return "WordSimilarity{" +
                "word='" + word + '\'' +
                ", similarity=" + similarity +
                ", rank=" + rank +
                '}';
    }
}
