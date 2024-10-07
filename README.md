# FinMate - Your AI Banking Queries Assistant

**FinMate** is a Spring Boot application designed to facilitate efficient email query tracking and management, utilizing the Gmail API and AI-powered automated responses. This application allows users to manage customer queries, send AI-generated replies, and track email interactions seamlessly.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [AI Integration](#ai-integration)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)
- [License](#license)

## Features

- Track email queries from customers.
- Use AI to generate automated replies, ensuring consistent and efficient responses.
- Manage ticketing for customer queries.
- Use OAuth 2.0 for secure Gmail API access.
- Log interactions for better customer service.

## Technologies Used

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- Gmail API
- OpenAI API for generating replies
- Jsoup for HTML parsing
- Lombok for boilerplate code reduction

## Installation

### Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL Database
- OpenAI API Key

### Steps

1. **Clone the repository:**

   ```bash
   git clone https://github.com/CodexParas/FinMate.git
   cd FinMate
   ```

2. **Update application properties:**

   Modify the `src/main/resources/application.properties` file with your PostgreSQL database credentials and OpenAI API key:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/yourdbname
   spring.datasource.username=yourusername
   spring.datasource.password=yourpassword
   spring.ai.openai.api-key=your-openai-api-key
   ```

3. **Install dependencies:**

   Run the following command to install the necessary dependencies:

   ```bash
   mvn clean install
   ```

4. **Run the application:**

   Start the application using:

   ```bash
   mvn spring-boot:run
   ```

5. **Access the API documentation:**

   Once the application is running, you can access the API documentation at
   `http://localhost:8080/api/v1/swagger-ui/index.html`.

### Dockerization (Optional)

To run the application in a Docker container:

1. **Build the Docker image:**

   ```bash
   docker build -t finmate-api .
   ```

2. **Run the Docker container:**

   ```bash
   docker run -p 8080:8080 finmate-api
   ```

## Usage

- The application exposes RESTful endpoints for interacting with email queries, ticket management, and sending replies.
- Ensure you have a valid OAuth 2.0 client ID and secret for accessing the Gmail API.

## AI Integration

The AI component of FinMate leverages OpenAI's API to generate contextually relevant responses to customer queries. This helps streamline customer support and provide prompt, accurate, and personalized responses.

- **Automated Replies**: When a customer query is received, FinMate analyzes the content and generates an appropriate response using AI. The generated response can either be sent automatically or reviewed by a human agent before sending.
- **Natural Language Processing**: Using OpenAI, the system can understand the intent and tone of customer emails, allowing for more empathetic and helpful replies.

To set up AI integration, ensure that the `spring.ai.openai.api-key` property in `application.properties` is set with your OpenAI API key.

## API Endpoints

### Email Queries

- `POST /api/email-query` - Insert a new email query.
- `GET /api/email-query/{id}` - Get details of an email query by ID.

### Tickets

- `POST /api/tickets` - Create a new ticket.
- `GET /api/tickets/{id}` - Get details of a ticket by ID.

### Authentication

- `POST /api/auth/login` - Authenticate user and return JWT token.

### AI Responses

- `POST /api/email-query/{id}/generate-response` - Generate an AI response for an email query.

## Contributors

- [Paras Gupta](https://github.com/CodexParas)

## Acknowledgments

Special thanks to the developers and maintainers of the technologies used in this project.

## Contributing

Contributions are welcome! Please submit a pull request or open an issue for discussion.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

### Instructions to Customize:

- Replace `yourusername`, `yourdbname`, `yourpassword`, `your-openai-api-key` and other placeholder values with actual information relevant to your project.
- Feel free to add more features, customize AI functionalities, or adjust the endpoints as per your specific requirements.
