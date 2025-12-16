package com.pigeon3.ssamantle.adapter.out.inmemory.game.dto;

import com.pigeon3.ssamantle.domain.model.game.vo.WordSimilarity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WordSimilarityRedisDto {
    private String word;
    private double similarity;
    private int rank;

    public static WordSimilarityRedisDto from(WordSimilarity wordSimilarity) {
        return WordSimilarityRedisDto.builder()
            .word(wordSimilarity.getWord())
            .similarity(wordSimilarity.getSimilarity())
            .rank(wordSimilarity.getRank())
            .build();
    }

    public WordSimilarity toDomain() {
        return WordSimilarity.of(word, similarity, rank);
    }
}
