# ChatBot Application

This is a simple chatbot application that interacts with the OpenAI API to generate responses based on user input. 
It uses Spring Boot for the backend and WebClient for making asynchronous API requests. 
The chatbot allows you to have an ongoing conversation, storing your conversation history and generating responses as the user interacts with it.

## Features

- **Chat functionality**: The user can input text and receive a response from the chatbot powered by OpenAI's model.
- **Conversation history**: The chat logs are stored in a conversation history that is used to provide context for the responses.
- **Retries on server errors**: The application automatically retries the request in case of server-side errors (5xx responses).
- **Error handling**: Proper error handling for both client-side errors (4xx) and server-side errors (5xx), including a custom error message for invalid response formats.

## Prerequisites

Before running the application, ensure you have the following:

- Java 17 or higher
- Maven or Gradle
- Access to the OpenAI API (you will need to provide an API key)

## Setup

### 1. Clone the repository
git clone https://github.com/michalradzik/chatbot-ai.git cd chatbot-ai


### 2. Add environment variables

The application uses environment variables to store sensitive information like the OpenAI API key and API URL. You can set these in your `.env` file (root directory).

OPENAI_API_KEY=your-api-key-here


### 3. Configure the application

Update the `application.properties` file to specify the OpenAI model:
openai.api.model=gpt-3.5-turbo


### 4. Build and run the application

If you're using Maven, you can build and run the application with the following commands:
mvn clean install mvn spring-boot:run

Ask your question (or type 'exit' to quit): How are you today? 
Bot: I'm doing great, thank you for asking!









