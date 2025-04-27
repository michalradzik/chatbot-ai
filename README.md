# 🤖 ChatBot Application (Spring Boot + OpenAI API)

This is a simple chatbot application that interacts with the OpenAI API to generate responses based on user input.  
It uses **Spring Boot** for the backend and **Spring AI Starter (`spring-ai-openai-spring-boot-starter`)** for seamless communication with OpenAI models.  
The chatbot enables ongoing conversation by maintaining history and handling API requests asynchronously.

---

## ✨ Features

- **Chat functionality**: Continuous real-time conversation with AI responses.
- **Conversation memory**: Maintains the context of previous interactions.
- **Error handling**: Gracefully manages API errors, timeouts, and invalid responses.
- **Simple setup**: Runs as a standalone Spring Boot console application.

---

## 🛠 Technologies Used

- Java 21
- Spring Boot 3
- Spring AI (`spring-ai-openai-spring-boot-starter`)
- Maven
- OpenAI API
- WebClient (for asynchronous HTTP requests)

---

## ⚙️ Prerequisites

Before running the application, ensure you have the following installed:

- Java 21 or higher
- Maven 3.8+
- Access to the OpenAI API (you will need your personal API key)

---

## 🚀 Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/michalradzik/chatbot-ai.git
cd chatbot-ai
```

### 2. Configure API Key

Set your OpenAI API key inside `src/main/resources/application.properties`:

```properties
openai.api.key=YOUR_OPENAI_API_KEY
```

---

### 3. Build and Run the Application

Using Maven:

```bash
mvn clean install
mvn spring-boot:run
```

---

## 💬 Example Interaction

```
Welcome to the AI Chatbot. Ask a question or type 'exit' to quit.

Your question: How are you today?
Bot: I'm doing great, thank you for asking!

Your question: Tell me a joke.
Bot: Why don't scientists trust atoms? Because they make up everything!
```

---

## 📌 Project Highlights

- **Integrated with OpenAI API via Spring AI starter** for efficient and scalable communication.
- **Asynchronous design** ensures smooth user experience without blocking.
- **Modular architecture** for easy future expansion (e.g., web UI frontend, conversation persistence).
- **Ready for deployment** or integration into larger AI-driven solutions.

---

## 📄 License

This project is licensed under the MIT License.

---

## 👌 Acknowledgments

Built as part of the **Programista 2.0** training program on practical AI application development.

---










