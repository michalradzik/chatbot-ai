# ChatBot Application

This is a simple chatbot application that interacts with the OpenAI API to generate responses based on user input. 
It uses Spring Boot for the backend and WebClient for making asynchronous API requests. 
The chatbot allows you to have an ongoing conversation, storing your conversation history and generating responses as the user interacts with it.

## Features

- **Chat functionality**: Users can input text and receive responses from an AI model.
- **Real-time interaction**: Continuous conversation with the chatbot through the console.
- **Error handling**: Handles API errors and invalid responses gracefully.
- **Simple setup**: Runs as a standalone Spring Boot console application
- 
## Prerequisites

Before running the application, ensure you have the following:

- Java 21 or higher
- Maven
- Access to the OpenAI API (you will need to provide an API key)

## Setup

### 1. Clone the repository
git clone https://github.com/michalradzik/chatbot-ai.git cd chatbot-ai


### 2. Configure API Key

The application uses the application.properties file to store your OpenAI API key.
Edit src/main/resources/application.properties and set your key:



### 3. Build and run the application

If you're using Maven, you can build and run the application with the following commands:
mvn clean install mvn spring-boot:run

Welcome to the AI Chatbot. Ask a question or type 'exit' to quit.
Your question: How are you today?
Bot: I'm doing great, thank you for asking!










