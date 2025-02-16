package com.radzik.service;

import com.radzik.api.Message;
import com.radzik.api.Request;
import com.radzik.api.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(args = {"testMode"})
public class ClientOpenAiTest {

    private static MockWebServer mockWebServer;

    @Autowired
    private ClientOpenAi clientOpenAi;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @BeforeEach
    void init() {
        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        ReflectionTestUtils.setField(clientOpenAi, "webClient", webClient);
    }

    @Test
    void sendMessage_Success() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setBody("{\"choices\": [{\"message\": {\"role\": \"user\", \"content\": \"Mock success response\"}}]}")
                .addHeader("Content-Type", "application/json"));

        Request request = new Request("model_name", List.of(new Message("user", "Hello")));
        Response response = clientOpenAi.sendMessage(request).block();

        assertNotNull(response);
        assertFalse(response.choices().isEmpty());
        assertEquals("Mock success response", response.choices().get(0).message().content());
    }

    @Test
    void sendMessage_ClientError() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.BAD_REQUEST.value())
                .setBody("{\"error\": \"Bad Request\"}")
                .addHeader("Content-Type", "application/json"));

        Request request = new Request("model_name", List.of(new Message("user", "Hello")));

        Mono<Response> responseMono = clientOpenAi.sendMessage(request);
        WebClientResponseException exception = assertThrows(WebClientResponseException.class, responseMono::block);

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getRawStatusCode());
    }


    @Test
    void sendMessage_ServerErrorWithRetry() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setBody("{\"error\": \"Server Error\"}")
                .addHeader("Content-Type", "application/json"));

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setBody("{\"error\": \"Server Error\"}")
                .addHeader("Content-Type", "application/json"));

        Request request = new Request("model_name", List.of(new Message("user", "Hello")));

        Mono<Response> responseMono = clientOpenAi.sendMessage(request);

        try {
            WebClientResponseException exception = assertThrows(WebClientResponseException.class,
                    () -> responseMono.block(Duration.ofSeconds(10))
            );
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getRawStatusCode());
        } catch (IllegalStateException e) {
            assertTrue(e.getCause() instanceof TimeoutException, "Expected TimeoutException as the cause of IllegalStateException");
            assertTrue(e.getMessage().contains("Timeout on blocking read"));
        }

        assertEquals(2, mockWebServer.getRequestCount());
    }
}
