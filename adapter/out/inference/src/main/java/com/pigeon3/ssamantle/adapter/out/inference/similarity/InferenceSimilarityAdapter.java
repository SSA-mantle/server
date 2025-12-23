package com.pigeon3.ssamantle.adapter.out.inference.similarity;

import com.pigeon3.ssamantle.adapter.out.inference.similarity.dto.SimilarityRequest;
import com.pigeon3.ssamantle.adapter.out.inference.similarity.dto.SimilarityResponse;
import com.pigeon3.ssamantle.application.game.port.out.CalculateSimilarityPort;
import com.pigeon3.ssamantle.domain.model.game.exception.GameDomainException;
import com.pigeon3.ssamantle.domain.model.game.exception.GameDomainExceptionType;
import com.pigeon3.ssamantle.domain.model.game.vo.WordSimilarity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 외부 추론 서버와 HTTP 통신하는 어댑터
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InferenceSimilarityAdapter implements CalculateSimilarityPort {

    private final WebClient webClient;

    @Value("${inference.api.endpoint:/api/similarity}")
    private String endpoint;

    @Override
    public WordSimilarity calculate(String answerWord, String guessWord) {
        log.info("Requesting similarity calculation for word: {}", guessWord);

        try {
            SimilarityRequest request = SimilarityRequest.of(guessWord);

            SimilarityResponse response = webClient.post()
                .uri(endpoint)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(SimilarityResponse.class)
                .block();

            if (response == null) {
                throw GameDomainException.of(GameDomainExceptionType.INFERENCE_SERVER_NO_RESPONSE);
            }

            log.info("Received similarity: date={}, answer={}, word={}, similarity={}",
                response.date(), response.answer(), response.word(), response.similarity());

            return response.toDomain();

        } catch (GameDomainException e) {
            // 도메인 예외는 그대로 전파 (없는 단어 등)
            throw e;
        } catch (Exception e) {
            log.error("Failed to calculate similarity for word: {}", guessWord, e);
            throw GameDomainException.of(
                GameDomainExceptionType.INFERENCE_SERVER_CALL_FAILED,
                "추론 서버 호출 실패: " + e.getMessage(),
                e
            );
        }
    }
}
