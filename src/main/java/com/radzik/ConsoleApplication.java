package com.radzik;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@SpringBootApplication
public class ConsoleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsoleApplication.class, args);
    }

    @Component
    @RequiredArgsConstructor
    public static class ConsoleRunner implements CommandLineRunner {

        private final ChatClient chatClient;


        @Override
        public void run(String... args) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Welcome to the AI application. Ask a question or type 'exit' to quit.");

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

            scanner.close();
        }
    }
}
