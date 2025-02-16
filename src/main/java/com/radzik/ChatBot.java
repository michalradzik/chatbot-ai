package com.radzik;

import com.radzik.api.Message;
import com.radzik.api.RequestData;
import com.radzik.api.Request;
import com.radzik.api.Response;
import com.radzik.service.ClientOpenAi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;

import java.util.*;

@Slf4j
@SpringBootApplication
public class ChatBot implements CommandLineRunner {

    private final ClientOpenAi clientOpenAi;

    public ChatBot(ClientOpenAi clientOpenAi) {
        this.clientOpenAi = clientOpenAi;
    }

    @Value("${openai.api.model}")
    String apiModel;


    public static void main(String[] args) {
        SpringApplication.run(ChatBot.class, args);
    }

    @Override
    public void run(String... args) {
        if (Arrays.asList(args).contains("testMode")) {
            return;
        }
        try (Scanner scanner = new Scanner(System.in)) {
            List<Message> conversationHistory = new ArrayList<>();
            String userInput;

            while (!(userInput = promptUserForInput(scanner)).equalsIgnoreCase("exit")) {
                conversationHistory.add(new Message("user", userInput));

                RequestData requestData = new RequestData(apiModel, conversationHistory);

                fetchChatResponse(requestData)
                        .doOnNext(this::displayChatResponse)
                        .block();
            }
        } catch (Exception e) {
            log.error("An error occurred during chat session", e);
        }
    }

    private Mono<Response> fetchChatResponse(RequestData requestData) {
        Request request = new Request(requestData.getModel(), requestData.getMessages());
        return clientOpenAi.sendMessage(request);
    }

    private void displayChatResponse(Response chatResponse) {
        Optional.ofNullable(chatResponse.choices())
                .orElse(Collections.emptyList())
                .stream()
                .map(choice -> choice.message().content())
                .forEach(log::info);
    }

    private String promptUserForInput(Scanner scanner) {
        log.info("Ask your question (or type 'exit' to quit):");
        return scanner.nextLine();
    }
}
