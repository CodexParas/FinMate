# FinMate

FinMate is a Spring Boot application designed to facilitate efficient email query tracking and management, utilizing the
Gmail API. This application allows users to manage customer queries, send automated replies, and track email
interactions seamlessly.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)
- [License](#license)

## Features

- Track email queries from customers.
- Send automated replies based on AI-generated responses.
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
- Jsoup for HTML parsing
- Lombok for boilerplate code reduction

## Installation

### Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL Database

### Steps

1. **Clone the repository:**

   ```bash
   git clone https://github.com/CodexParas/FinMate.git
   cd FinMate
   ```

2. **Update application properties:**

   Modify the `src/main/resources/application.properties` file with your PostgreSQL database credentials:

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
   docker build -t FinMate-api .
   ```

2. **Run the Docker container:**

   ```bash
   docker run -p 8080:8080 FinMate-api
   ```

## Usage

- The application exposes RESTful endpoints for interacting with email queries, ticket management, and sending replies.
- Ensure you have a valid OAuth 2.0 client ID and secret for accessing the Gmail API.

## API Endpoints

### Email Queries

- `POST /api/email-query` - Insert a new email query.
- `GET /api/email-query/{id}` - Get details of an email query by ID.

### Tickets

- `POST /api/tickets` - Create a new ticket.
- `GET /api/tickets/{id}` - Get details of a ticket by ID.

### Authentication

- `POST /api/auth/login` - Authenticate user and return JWT token.

## Contributors

- [Paras Gupta](https://github.com/CodexParas)

## Acknowledgments

Special thanks to the developers and maintainers of the technologies used in this project.

## Contributing

Contributions are welcome! Please submit a pull request or open an issue for discussion.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

### Instructions to Customize:

- Replace `yourusername`, `yourdbname`, `yourpassword`, `your-openai-api-key` and other placeholder values with actual
  information relevant to
  your project.
- You may want to add additional sections for features, API examples, or anything else specific to your application.

Feel free to modify it further or let me know if you want to add anything specific!