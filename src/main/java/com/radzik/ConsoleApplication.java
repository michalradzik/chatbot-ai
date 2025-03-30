package com.radzik;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@SpringBootApplication
@Component
@RequiredArgsConstructor
public class ConsoleApplication implements CommandLineRunner {

    private final ChatClient chatClient;

    public static void main(String[] args) {
        SpringApplication.run(ConsoleApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Welcome to the AI application. Ask a question or type 'exit' to quit.");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Your question: ");
                String userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting application...");
                    break;
                }

                try {
                    String response = chatClient.call(userInput);
                    System.out.println("AI's response: " + response);
                } catch (Exception e) {
                    System.err.println("An error occurred while processing the request.");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while setting up the scanner: " + e.getMessage());
        }
    }
}