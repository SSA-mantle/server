package com.pigeon3.ssamantle.adapter.out.inference.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 추론 서버와 통신하기 위한 WebClient 설정
 */
@Configuration
public class InferenceWebClientConfig {

    @Value("${inference.api.base-url}")
    private String baseUrl;

    @Value("${inference.api.timeout:5000}")
    private int timeout;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }
}
