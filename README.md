# Pidima Coding Challenge

This repository contains a complete microservice implementation with comprehensive documentation and architecture diagrams.

## 📋 Submission Deliverables

✅ **Complete microservice code** - Fully functional Spring Boot chat microservice  
✅ **README with setup and run instructions** - Comprehensive setup guide  
✅ **Architecture diagram (PNG format)** - Visual system architecture  
✅ **System description document** - Complete technical documentation  

## 📁 Repository Structure

### 📚 Documentation
- `README.md` - Main repository documentation and setup instructions
- `ARCHITECTURE.md` - Detailed architecture documentation with diagrams
- `SYSTEM_DESCRIPTION.md` - Comprehensive system description document
- `architecture_diagram.png` - Main system architecture diagram
- `deployment_diagram.png` - Deployment architecture diagram  
- `technology_stack.png` - Technology stack visualization

### 💻 Source Code

## Directory Structure

```
chatbot-service/
├── src/
│   ├── main/java/com/pidima/chatmicroservice/
│   │   ├── api/          # REST API endpoints, DTOs, and exception handling
│   │   ├── models/       # Domain models and entities
│   │   ├── services/     # Business logic services
│   │   └── config/       # Application configuration and main class
│   └── test/java/com/pidima/chatmicroservice/
│       ├── api/          # Tests for API layer (following Java naming conventions)
│       └── services/     # Tests for service layer (following Java naming conventions)
├── Dockerfile
├── pom.xml               # Maven configuration (Java equivalent of requirements.txt)
└── README.md
```

## Running the Application

Navigate to the `chatbot-service` directory and run:

```bash
cd chatbot-service
./mvnw spring-boot:run
```

## Running Tests

```bash
cd chatbot-service
./mvnw test
```

## Features

- **Complete Architecture Documentation**: Detailed system design and component diagrams
- **Comprehensive System Description**: Technical documentation covering all aspects
- **Visual Architecture Diagrams**: Professional system architecture illustrations
- **Organized Structure**: Code is logically separated into api, models, services, and config packages
- **Java Naming Conventions**: Tests follow proper Java package naming
- **Maven Support**: Uses pom.xml for dependency management
- **Docker Support**: Complete containerization available
- **Comprehensive Testing**: All tests pass with the new structure

## 📊 Architecture & Design

### System Architecture
The microservice follows a layered architecture pattern:
- **API Layer**: REST controllers and DTOs
- **Service Layer**: Business logic implementation  
- **Repository Layer**: Data access abstraction
- **Model Layer**: Domain entities

See `ARCHITECTURE.md` for detailed architecture documentation and `architecture_diagram.png` for visual representation.

### Technology Stack
- **Java 17** with **Spring Boot 3.2.0**
- **Maven** for build management
- **Docker** for containerization
- **JUnit 5** for testing

See `technology_stack.png` for complete technology stack visualization.

## 📖 Documentation

- **`ARCHITECTURE.md`** - Complete architecture documentation with ASCII diagrams
- **`SYSTEM_DESCRIPTION.md`** - Comprehensive system description covering:
  - System overview and business value
  - Detailed API specifications  
  - Data models and relationships
  - Technology stack details
  - Security considerations
  - Performance and scalability
  - Deployment strategies
  - Monitoring and observability
  - Testing strategies
  - Future enhancement roadmap

## 🖼️ Visual Diagrams

- **`architecture_diagram.png`** - Main system architecture diagram
- **`deployment_diagram.png`** - Container and deployment architecture  
- **`technology_stack.png`** - Technology stack visualization