package com.radzik.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.radzik.api.Request;
import com.radzik.api.Response;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Service
public class ClientOpenAi {

    private final WebClient webClient;
    String apiKey = Dotenv.load().get("OPENAI_API_KEY");

    public ClientOpenAi(WebClient.Builder webClientBuilder, @Value("${openai.api.url}") String apiUrl) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
    }

    public Mono<Response> sendMessage(Request chatRequest) {
        return webClient.post()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(chatRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    log.warn("Client error: Status Code {}", response.statusCode());
                    return Mono.error(new WebClientResponseException(
                            response.statusCode().value(), "Client error", null, null, null));
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    log.error("Server error: Status Code {}", response.statusCode());
                    return Mono.error(new WebClientResponseException(
                            response.statusCode().value(), "Server error", null, null, null));
                })
                .bodyToMono(Response.class)
                .onErrorMap(JsonParseException.class, e -> {
                    log.error("Invalid response format: {}", e.getMessage());
                    return new RuntimeException("Invalid response format", e);
                })
                .retryWhen(Retry.fixedDelay(1, Duration.ofSeconds(1))
                        .filter(throwable -> {
                            if (throwable instanceof WebClientResponseException we) {
                                boolean shouldRetry = we.getStatusCode().is5xxServerError();
                                if (shouldRetry) {
                                    log.warn("Retrying request due to server error (status {}): {}", we.getStatusCode(), we.getMessage());
                                }
                                return shouldRetry;
                            }
                            return false;
                        })
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            log.error("Max retries reached, failing the request");
                            return new WebClientResponseException("Max retries reached, server error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Internal Server Error", null, null, null);
                        }))
                .doOnError(error -> log.error("Final failure during API call: {}", error.getMessage()));
    }
}
