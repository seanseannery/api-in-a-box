# API-IN-A-BOX

## What is this project?

This project implements an example backend REST API service that can be copied as a template for engineers to spin up new services quickly, and used as a template to accelerate the creation of new backend services. It takes an opinionated approach at implementing common functionality for large scale including: configuration, rate limiting, circuit breaking, logging, authorization and metrics and container infrastructure using popular libraries and technologies. It implements these so that the engineer spinning up their service can focus on business logic not common repeatable live operational best practices.

## Technologies Used

- **Spring Boot 3.5.0** - Core framework with Java 21
- **Spring Security** - OAuth2/JWT authentication and authorization
- **Protocol Buffers** - API contract definitions and serialization (DTOs)
- **Docker & Docker Compose** - Containerization and local development
- **Maven** - Build and dependency management
- **Swagger/OpenAPI** - API documentation
- **BCrypt** - Password hashing
- **Lombok** - Boilerplate code reduction (DAOs)

## Folder Structure

```
├── infra/                  # Infrastructure deployment and local setup scripts
│   ├── Dockerfile         # Container build configuration
├── src/main/java/com/apiinabox/
│   ├── account/           # Account CRUD API implementation
│   │   ├── api/          # API interfaces and Protocol Buffer DTOs
│   │   ├── controller/   # REST controllers and services
│   │   ├── model/        # Domain models and mappers
│   │   └── repository/   # Data access layer (mock implementation)
│   ├── book/             # Book CRUD API implementation (similar structure)
│   ├── config/           # Application configuration classes
│   ├── security/         # Security configuration and JWT handling
│   └── helloworld/       # Simple hello world example endpoint
├── src/main/resources/    # Application properties and configuration
├── target/               # Maven build output
├── Makefile              # Developer workflow commands
├── pom.xml               # Maven project configuration
└── test-api.sh           # API testing script
```

## Development Environment Setup

### Prerequisites
- macOS (automated setup provided)
- Terminal with bash or zsh

### Quick Setup
Run the automated setup command to install all required dependencies:

```bash
make setup
```

This will automatically install:
- Homebrew (if not present)
- Java 21 with jenv for version management
- Maven
- Docker and Docker Compose

After setup completes, restart your terminal or run:
```bash
source ~/.zshrc  # or ~/.bashrc depending on your shell
```

### Manual Verification
Verify your environment is ready:
```bash
java -version    # Should show Java 21
mvn -version     # Should show Maven
docker --version # Should show Docker
```

## Running the Project

### Start the Application
Build and run the application in Docker:
```bash
make run
```

This command will:
1. Build the Docker image
2. Start the application on port 8080
3. Run in detached mode

### View Application Logs
```bash
make docker-logs
```

### Access the Application
- **API Base URL**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Open Swagger in Browser**: `make open-swagger`

### Test the APIs
Run the provided test script to verify all endpoints:
```bash
./test-api.sh
```

### Stop the Application
```bash
make docker-stop
```

### Development Commands
```bash
	  make setup         - Setup development environment (macOS)
	  make build         - Build the project
	  make docker-build  - Build Docker image
	  make run           - Build and run the application in Docker (8080)
	  make docker-logs   - Show container logs
	  make open-swagger  - Open Swagger UI in browser
	  make docker-shell  - Attach to the running container's shell
	  make docker-stop   - Stop Docker containers
	  make all           - Clean, build and run the application (default)
	  make help          - Show this help message"
```
## API Examples

The project includes two example CRUD APIs:

### Account API (`/api/accounts`)
- Create, read, update, delete accounts
- Password management with secure hashing
- Authentication endpoint
- No authorization required (configurable)

### Book API (`/api/books`)
- Full CRUD operations with role-based access control
- Requires JWT authentication when enabled
- Different permissions for ADMIN, WRITER, READER roles

### Configuration
Authorization can be enabled/disabled in `src/main/resources/application.properties`:
```properties
app.authorization-enabled=false  # Set to true to enable JWT auth
```