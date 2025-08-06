# Technologies Used

## Core Service Technologies
- **Java** - Programming language
- **Spring Boot** - Core API service framework
- **Spring Security** - OAuth2/JWT authentication and authorization
- **Protocol Buffers** - API contract definitions and serialization (DTOs)
- **Swagger/OpenAPI** - API documentation
- **BCrypt** - Password hashing
- **Lombok** - Boilerplate code reduction (DAOs)
- **Open Telemetry** - Metrics and logging framework for application monitoring

## Lifecycle Management and CI/CD
- **Make** - Used to create common developer commands that wrap other build, development, and local execution tools for a common developer interface
- **Github Actions** - CI/CD build, test, package, publish and deploy steps.
- **Flyway** - Database Migrations

## Testing Technologies
- **JUnit** - Unit and Integration Testing
- **Mockito** - Mocking Dependencies in Unit and Integration Testing
- **Gatling** - Load tests
- **AWS Lambdas** - Synthetic traffic testing

## Infrastructure
- **Terraform** - AWS Cloud Infrastructure provisioning
- **Docker** - Containerization
- **docker-compose** - container orchestration for local build and execution
- **PostgreSQL** - Database storage



Kiro and Developers working on the project should try to use the most recent version of dependencies unless it is incompatible with Java24 or other dependencies.
Dependency versions should be explicitly used and declared in the `pom.xml` file.
