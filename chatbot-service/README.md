# Pidima Chat Microservice

A minimalistic Spring Boot microservice that provides chat session management with REST APIs for creating sessions, sending messages, and retrieving conversation history.

## Features

- **Session Management**: Create and manage chat sessions
- **Message Handling**: Send and retrieve messages within sessions  
- **Request/Response Validation**: Bean validation for all API inputs
- **Error Handling**: Global exception handling with detailed error responses
- **Health Monitoring**: Health check endpoint with system metrics
- **Logging**: Comprehensive logging with configurable levels
- **Configuration Management**: YAML-based configuration
- **Docker Support**: Complete containerization with multi-stage builds
- **Unit Testing**: Comprehensive test coverage for controllers and services

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Web** - REST API framework
- **Spring Validation** - Request/response validation
- **Spring Boot Actuator** - Health monitoring
- **Maven** - Build and dependency management
- **JUnit 5** - Unit testing
- **Docker** - Containerization

## API Endpoints

### 1. Create Chat Session
**POST** `/chat/session`

Creates a new chat session with optional initial message.

**Request Body:**
```json
{
  "initial_message": "Hello, world!" // Optional, max 100 characters
}
```

**Response:**
```json
{
  "session_id": "uuid-string",
  "created_at": "2025-09-21T09:51:23.795867663"
}
```

### 2. Send Message  
**POST** `/chat/message`

Sends a message to an existing chat session.

**Request Body:**
```json
{
  "session_id": "uuid-string",        // Required
  "content": "Your message here",     // Required, max 500 characters
  "sender": "username"                // Required, max 50 characters
}
```

**Response:**
```json
{
  "message_id": "uuid-string",
  "session_id": "uuid-string", 
  "content": "Your message here",
  "sender": "username",
  "timestamp": "2025-09-21T09:51:23.795867663"
}
```

### 3. Get Chat History
**GET** `/chat/history/{session_id}`

Retrieves all messages for a specific chat session.

**Response:**
```json
[
  {
    "message_id": "uuid-string",
    "session_id": "uuid-string",
    "content": "Message content",
    "sender": "username", 
    "timestamp": "2025-09-21T09:51:23.795867663"
  }
]
```

### 4. Health Check
**GET** `/health`

Returns service health status and metrics.

**Response:**
```json
{
  "status": "UP",
  "service": "chat-microservice",
  "version": "0.0.1-SNAPSHOT",
  "timestamp": "2025-09-21T09:51:23.795867663",
  "metrics": {
    "active_sessions": 5
  }
}
```

## Error Handling

The API provides structured error responses:

```json
{
  "status": 400,
  "message": "Validation failed", 
  "timestamp": "2025-09-21T09:51:23.795867663",
  "validation_errors": {
    "field_name": "Error message"
  }
}
```

Common HTTP status codes:
- `200` - Success
- `201` - Created (new session/message)
- `400` - Bad Request (validation errors, session not found)
- `500` - Internal Server Error

## Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+ or use included Maven wrapper
- Docker (optional, for containerization)

### Running Locally

1. **Clone the repository:**
   ```bash
   git clone https://github.com/youssefhatem124/Pidima-coding-challenge.git
   cd Pidima-coding-challenge
   ```

2. **Build and run with Maven:**
   ```bash
   ./mvnw spring-boot:run
   ```
   
   Or build and run the JAR:
   ```bash
   ./mvnw clean package
   java -jar target/chat-microservice-0.0.1-SNAPSHOT.jar
   ```

3. **The service will start on http://localhost:8080**

### Running Tests

```bash
# Run all tests
./mvnw test

# Run tests with coverage
./mvnw test jacoco:report
```

### Docker Deployment

1. **Build Docker image:**
   ```bash
   docker build -t chat-microservice .
   ```

2. **Run container:**
   ```bash
   docker run -p 8080:8080 chat-microservice
   ```

3. **With health check:**
   ```bash
   docker run -p 8080:8080 --health-cmd="curl -f http://localhost:8080/health" chat-microservice
   ```

## API Usage Examples

### Creating a session and sending messages:

```bash
# 1. Create a new session
RESPONSE=$(curl -s -X POST http://localhost:8080/chat/session \
  -H "Content-Type: application/json" \
  -d '{"initial_message": "Hello, world!"}')

SESSION_ID=$(echo $RESPONSE | jq -r '.session_id')
echo "Created session: $SESSION_ID"

# 2. Send a message
curl -X POST http://localhost:8080/chat/message \
  -H "Content-Type: application/json" \
  -d "{
    \"session_id\": \"$SESSION_ID\",
    \"content\": \"How are you today?\",
    \"sender\": \"user123\"
  }"

# 3. Get chat history
curl -X GET http://localhost:8080/chat/history/$SESSION_ID

# 4. Check health
curl -X GET http://localhost:8080/health
```

## Configuration

The application can be configured via `application.yml`:

```yaml
server:
  port: 8080                    # Server port

spring:
  application:
    name: chat-microservice     # Application name

logging:
  level:
    com.pidima.chatmicroservice: INFO    # Application log level
    org.springframework: WARN            # Spring log level
```

## Architecture

The microservice follows a layered architecture:

```
├── controller/          # REST API endpoints
├── service/            # Business logic
├── model/              # Domain models
├── dto/                # Data transfer objects
├── exception/          # Error handling
└── config/             # Configuration
```

**Design Principles:**
- **Separation of Concerns**: Clear layer separation
- **Dependency Injection**: Spring-managed beans
- **Validation**: Bean validation for inputs
- **Error Handling**: Centralized exception handling
- **Testing**: Comprehensive unit test coverage
- **Security**: Non-root Docker user, input validation

## Storage

Currently uses in-memory storage for simplicity. In production, this should be replaced with:
- Database (PostgreSQL, MySQL)
- Redis for caching
- Message queues for async processing

## Monitoring

Health endpoint provides:
- Service status
- Active session count
- Version information
- Timestamp

Additional monitoring can be added via Spring Boot Actuator endpoints.

## Development

### Project Structure
```
src/
├── main/java/com/pidima/chatmicroservice/
│   ├── ChatMicroserviceApplication.java
│   ├── controller/
│   ├── service/
│   ├── model/
│   ├── dto/
│   └── exception/
├── main/resources/
│   └── application.yml
└── test/java/
    └── com/pidima/chatmicroservice/
```

### Adding New Features

1. Create DTOs for request/response
2. Add validation annotations
3. Implement service logic
4. Create controller endpoints
5. Add unit tests
6. Update documentation

## Future Enhancements

- Persistent storage integration
- Authentication and authorization
- Rate limiting
- Message queuing
- WebSocket support for real-time chat
- API versioning
- Metrics and monitoring dashboard
- Distributed tracing

## License

This project is created for the Pidima coding challenge.
