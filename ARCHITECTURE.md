# Pidima Chat Microservice - System Architecture

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          CLIENT APPLICATIONS                                │
│                        (Web, Mobile, API clients)                           │
└─────────────────────────────┬───────────────────────────────────────────────┘
                              │ HTTP/REST API
                              │
┌─────────────────────────────▼───────────────────────────────────────────────┐
│                        LOAD BALANCER                                        │
│                     (Optional - Production)                                 │
└─────────────────────────────┬───────────────────────────────────────────────┘
                              │ HTTP/REST API
                              │
┌─────────────────────────────▼───────────────────────────────────────────────┐
│                    PIDIMA CHAT MICROSERVICE                                 │
│                          (Spring Boot)                                      │
│                                                                             │
│  ┌───────────────────────────────────────────────────────────────────────┐  │
│  │                        API LAYER                                     │  │
│  │                                                                       │  │
│  │  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────────┐   │  │
│  │  │  ChatController │  │ HealthController│  │ GlobalException     │   │  │
│  │  │                 │  │                 │  │ Handler             │   │  │
│  │  │ /chat/session   │  │ /health         │  │                     │   │  │
│  │  │ /chat/message   │  │                 │  │ Centralized Error   │   │  │
│  │  │ /chat/history   │  │                 │  │ Handling            │   │  │
│  │  └─────────────────┘  └─────────────────┘  └─────────────────────┘   │  │
│  └───────────────────────────┬───────────────────────────────────────────┘  │
│                              │                                              │
│  ┌───────────────────────────▼───────────────────────────────────────────┐  │
│  │                       DTO LAYER                                      │  │
│  │                                                                       │  │
│  │  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────────┐   │  │
│  │  │CreateSession    │  │SendMessage      │  │ErrorResponse        │   │  │
│  │  │Request/Response │  │Request/Response │  │                     │   │  │
│  │  └─────────────────┘  └─────────────────┘  └─────────────────────┘   │  │
│  └───────────────────────────┬───────────────────────────────────────────┘  │
│                              │                                              │
│  ┌───────────────────────────▼───────────────────────────────────────────┐  │
│  │                     SERVICE LAYER                                    │  │
│  │                                                                       │  │
│  │  ┌─────────────────────────────────────────────────────────────────┐ │  │
│  │  │                    ChatService                                  │ │  │
│  │  │                                                                 │ │  │
│  │  │  • createSession()                                              │ │  │
│  │  │  • sendMessage()                                                │ │  │
│  │  │  • getChatHistory()                                             │ │  │
│  │  │  • getSessionCount()                                            │ │  │
│  │  │                                                                 │ │  │
│  │  │  Business Logic & Validation                                    │ │  │
│  │  └─────────────────────────────────────────────────────────────────┘ │  │
│  └───────────────────────────┬───────────────────────────────────────────┘  │
│                              │                                              │
│  ┌───────────────────────────▼───────────────────────────────────────────┐  │
│  │                   REPOSITORY LAYER                                   │  │
│  │                                                                       │  │
│  │  ┌─────────────────────┐  ┌─────────────────────┐                     │  │
│  │  │ChatSessionRepository│  │ChatMessageRepository│                     │  │
│  │  │                     │  │                     │                     │  │
│  │  │ Spring Data JPA     │  │ Spring Data JPA     │                     │  │
│  │  │ Interface           │  │ Interface           │                     │  │
│  │  └─────────────────────┘  └─────────────────────┘                     │  │
│  └───────────────────────────┬───────────────────────────────────────────┘  │
│                              │                                              │
│  ┌───────────────────────────▼───────────────────────────────────────────┐  │
│  │                      MODEL LAYER                                     │  │
│  │                                                                       │  │
│  │  ┌─────────────────┐  ┌─────────────────┐                             │  │
│  │  │   ChatSession   │  │   ChatMessage   │                             │  │
│  │  │                 │  │                 │                             │  │
│  │  │ - sessionId     │  │ - messageId     │                             │  │
│  │  │ - timestamp     │  │ - sessionId     │                             │  │
│  │  │ - metadata      │  │ - content       │                             │  │
│  │  │                 │  │ - sender        │                             │  │
│  │  │                 │  │ - timestamp     │                             │  │
│  │  └─────────────────┘  └─────────────────┘                             │  │
│  └───────────────────────────┬───────────────────────────────────────────┘  │
└─────────────────────────────┬┴───────────────────────────────────────────────┘
                              │
┌─────────────────────────────▼───────────────────────────────────────────────┐
│                        DATA STORAGE                                         │
│                      (In-Memory Maps)                                       │
│                                                                             │
│  ┌─────────────────────┐  ┌─────────────────────┐                           │
│  │   Session Store     │  │   Message Store     │                           │
│  │  ConcurrentHashMap  │  │  ConcurrentHashMap  │                           │
│  │                     │  │                     │                           │
│  │  Key: sessionId     │  │  Key: sessionId     │                           │
│  │  Value: ChatSession │  │  Value: List<       │                           │
│  │                     │  │         ChatMessage>│                           │
│  └─────────────────────┘  └─────────────────────┘                           │
└─────────────────────────────────────────────────────────────────────────────┘
```

## Component Relationships

### Data Flow
1. **Client Request** → API Layer (Controllers)
2. **API Layer** → DTO Layer (Request/Response validation)
3. **DTO Layer** → Service Layer (Business logic)
4. **Service Layer** → Repository Layer (Data access)
5. **Repository Layer** → Model Layer (Domain objects)
6. **Model Layer** → Data Storage (In-memory persistence)

### Key Components

#### API Layer
- **ChatController**: Main REST endpoints for chat operations
- **HealthController**: System health monitoring
- **GlobalExceptionHandler**: Centralized error handling

#### Service Layer
- **ChatService**: Core business logic for chat operations

#### Repository Layer
- **ChatSessionRepository**: Session data access
- **ChatMessageRepository**: Message data access

#### Model Layer
- **ChatSession**: Domain model for chat sessions
- **ChatMessage**: Domain model for chat messages

### Cross-Cutting Concerns
- **Validation**: Bean validation using Jakarta Validation
- **Logging**: SLF4J with configurable levels
- **Error Handling**: Global exception handling with detailed responses
- **Health Monitoring**: Actuator endpoints for system metrics
- **Configuration**: YAML-based application configuration

## Deployment Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                       DOCKER CONTAINER                          │
│                                                                 │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │             JVM (OpenJDK 17)                              │  │
│  │                                                           │  │
│  │  ┌─────────────────────────────────────────────────────┐  │  │
│  │  │        Spring Boot Application                      │  │  │
│  │  │                                                     │  │  │
│  │  │  • Embedded Tomcat Server (Port 8080)              │  │  │
│  │  │  • Chat Microservice                               │  │  │
│  │  │  • Health Endpoints                                │  │  │
│  │  │                                                     │  │  │
│  │  └─────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                 │
│  Security: Non-root user (appuser)                              │
│  Health Check: /health endpoint via curl                        │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
         │
         ▼ Port 8080
┌─────────────────────────────────────────────────────────────────┐
│                      HOST SYSTEM                                │
│                                                                 │
│  Docker Engine                                                  │
│  • Multi-stage build optimization                               │
│  • Automated health checks                                      │
│  • Resource constraints (configurable)                          │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

## Technology Stack Visualization

```
┌─────────────────────────────────────────────────────────────────┐
│                    FRAMEWORK STACK                              │
├─────────────────────────────────────────────────────────────────┤
│  Spring Boot 3.2.0        │  Primary application framework      │
│  Spring Web               │  REST API framework                 │
│  Spring Validation        │  Bean validation                    │
│  Spring Boot Actuator     │  Health monitoring                  │
├─────────────────────────────────────────────────────────────────┤
│                    RUNTIME STACK                               │
├─────────────────────────────────────────────────────────────────┤
│  Java 17                  │  Runtime environment                │
│  Embedded Tomcat          │  Web server                         │
│  Maven 3.6+               │  Build tool                         │
├─────────────────────────────────────────────────────────────────┤
│                    TESTING STACK                               │
├─────────────────────────────────────────────────────────────────┤
│  JUnit 5                  │  Unit testing framework             │
│  Mockito                  │  Mocking framework                  │
│  Spring Boot Test         │  Integration testing                │
├─────────────────────────────────────────────────────────────────┤
│                  CONTAINERIZATION                              │
├─────────────────────────────────────────────────────────────────┤
│  Docker                   │  Container platform                 │
│  Multi-stage builds       │  Optimized image creation           │
│  Health checks            │  Container monitoring               │
└─────────────────────────────────────────────────────────────────┘
```