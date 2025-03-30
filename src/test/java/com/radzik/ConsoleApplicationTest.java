package com.radzik;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ai.chat.ChatClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ConsoleApplicationTest {

    @Mock
    private ChatClient chatClient;

    @InjectMocks
    private ConsoleApplication consoleRunner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldExitApplicationWhenUserEntersExitCommand() {
        // Given
        ByteArrayInputStream in = new ByteArrayInputStream("exit\n".getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // When
        consoleRunner.run();

        // Then
        assertTrue(outContent.toString().contains("Exiting application..."));
    }

    @Test
    void shouldReturnValidChatResponseWhenUserAsksAQuestion() {
        // Given
        ByteArrayInputStream in = new ByteArrayInputStream("Hello?\nexit\n".getBytes());
        System.setIn(in);

        when(chatClient.call("Hello?")).thenReturn("Hi there!");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // When
        consoleRunner.run();

        // Then
        assertTrue(outContent.toString().contains("AI's response: Hi there!"));
        verify(chatClient, times(1)).call("Hello?");
    }
}
