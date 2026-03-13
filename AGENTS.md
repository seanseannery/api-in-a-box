
# Project Overview

This project implements an example backend REST API service that can be copied as a template for engineers to spin up new services quickly, and used  to accelerate the creation of new backend services. It takes an opinionated approach at implementing common functionality for large scale including: configuration, rate limiting, circuit breaking, logging, authorization and metrics and container infrastructure using popular libraries and technologies. It implements these so that the engineer spinning up their service can focus on business logic not common repeatable live operational best practices.

# Commands

```bash
make build         # mvn clean package
make run           # build Docker image and run on port 8080
make docker-stop   # stop running containers
make docker-logs   # view container logs
make docker-shell  # access running container shell
```

Tests:
```bash
mvn test                                              # all tests
mvn test -Dtest=RateLimitServiceTest                  # single class
mvn test -Dtest=RateLimitServiceTest#testApiRateLimit # single method
```

Swagger UI is available at `http://localhost:8080/swagger-ui.html` when running.

# Architecture

Spring Boot 3.5 / Java 25 REST API template. OpenAPI standards and Protobufs for endpoint naming. In-memory mock repositories — no real database. Resilianace4j for rate-limiting

## Folder Structure

```
├── infra/                  # Infrastructure deployment and local setup scripts
│   ├── Dockerfile         # Container build configuration
│   ├── docker-compose.yml # Container orchestration for local development
│   └── local-setup.mk    # macOS dev environment setup (included by Makefile)
├── src/main/java/com/apiinabox/
│   ├── account/           # Account CRUD API implementation
│   │   ├── api/          # API interfaces
│   │   │   └── dto/      # Protocol Buffer DTOs (.proto source files)
│   │   ├── controller/   # REST controllers and services
│   │   ├── model/        # Domain models and mappers
│   │   └── repository/   # Data access layer (mock implementation)
│   ├── book/             # Book CRUD API implementation (similar structure)
│   ├── config/           # Application configuration classes
│   ├── security/         # Security configuration and JWT handling
│   ├── ratelimit/        # Rate limiting interceptor, service, and result types
│   └── common/           # Shared utilities across domains (planned)
├── src/main/resources/    # Application properties and configuration
├── target/               # Maven build output
├── Makefile              # Developer workflow commands
├── pom.xml               # Maven project configuration
└── test-api.sh           # API testing script
```

## Technologies Used

- **Spring Boot 3.5.0** - Core framework with Java 25
- **Spring Security** - OAuth2/JWT authentication and authorization
- **Resilience4j** - Rate limiting (RateLimiter) and circuit breaking
- **Protocol Buffers / gRPC** - API contract definitions and DTO serialization
- **Docker & Docker Compose** - Containerization and local development
- **Maven** - Build and dependency management
- **Swagger/OpenAPI** - API documentation (springdoc-openapi)
- **BCrypt** - Password hashing
- **Lombok** - Boilerplate code reduction

### Planned / Roadmap Technologies

The following are planned additions per the project roadmap (not yet implemented):

- **Open Telemetry** - Metrics and distributed tracing
- **Flyway** - Database schema migrations
- **PostgreSQL** - Persistent database (replacing in-memory mocks)
- **Gatling** - Load and performance testing
- **Terraform** - Cloud infrastructure provisioning (AWS)
- **AWS Lambda** - Synthetic traffic/canary testing

## Module Structure

Each domain module (e.g. `account`, `book`) follows this pattern:
- `api/` — API interface (Java interface) + `dto/` subfolder for Protocol Buffer `.proto` files
- `controller/` — REST controller implementing the API interface
- `model/` — domain model + mapper (model ↔ DTO)
- `repository/` — data access layer (currently mock in-memory, ArrayList-backed)

## Cross-Cutting Modules

**`config/`** — `ApplicationConfig` and `AuthorizationConfig` read `app.*` properties (authorization toggle, rate limiting config).

**`security/`** — `SecurityConfig` builds a conditional filter chain:
- When `app.authorization-enabled=false` (default): all endpoints public, CSRF disabled
- When `true`: OAuth2 JWT validation, `/api/books/**` requires `USER` role

**`ratelimit/`** — Resilience4j-backed rate limiting applied via `RateLimitInterceptor` on all `/api/**` requests (except `/api/health`, `/api/status`). Three limit dimensions: per-endpoint, per-IP, per-client. Client identity resolved from `X-API-Key` header → JWT subject → IP address. Limits configured under `resilience4j.ratelimiter` in `application.yml`.

## Key Config Flags (application.yml)

| Property | Default | Effect |
|---|---|---|
| `app.authorization-enabled` | `false` | Enables JWT auth |
| `app.rate-limiting.enabled` | `true` | Enables rate limiting |

# Agent Behaviour

  ## Contributing Code and Features

  Must Read (if you havent already) and adhere to `CONTRIBUTING.md` for style, design choices, and code contribution guidelines. High priority contribution guidelines include: 
  
  - Prefer readability over micro-optimization: clear code is more important than saving microseconds
  - Prefer standard library functions and utilities over reimplementing the wheel yourself, the standard library is extensive.
  - Only use external dependencies if it improves code simplicity/security and has a very active community, otherwise prefer the standard library
  - Use domain driven design, and MVC architectures 
  - Follow trunk-based development flow, using feature branches and conventional commit standards for commit and pr title naming
    - **Commits** — use [Conventional Commits](https://www.conventionalcommits.org/): `<type>(<scope>): <summary>`. Types: `feat`, `fix`, `refactor`, `test`, `docs`, `chore`, `ci`. Subject line under 72 characters, imperative mood.
    - Before committing, check the current branch name and verify it is scoped to the work being committed. If the current branch is `main` or is focused on a different feature/topic than the changes being committed, create a new appropriately-named branch first (following the naming conventions in CONTRIBUTING.md) before committing. New branches must always be created off of `main` unless the user explicitly instructs otherwise.


  ## Must Do
  - For changes touching more than one non-test .java file: state the approach before writing code. If it is a new feature, ask if new documentation should be created in /docs folder
  - check the Java version in `pom.xml` and use idiomatic features and libraries available at that version
  - Always check for remote code changes before starting development or committing changes.  Prefer rebase from remote `main` into the current feature branch by using `git pull --rebase origin main`. Dont resolve conflicts, instead prompt the user.

  ## Must NOT Do
  - do not add new heavy dependencies without approval. do not use dependencies that have small, inactive communities or known vulnerabilites.
  - Never lower the quality or coverage of an existing test to make a broken feature work. If a change requires doing this, prompt for approval before proceeding.
  - Never read or load LICENSE file into context unless explicitly asked
  - Do NOT include a `Co-Authored-By` trailer in commit messages or wrap in a HEREDOC unnecessarily
  - Tests must not pin to values that change between releases (version strings, build timestamps). Validate shape/format instead (e.g. semver regex, non-empty check).
  - Never resolve git merge or rebase conflicts without user input.

  ## Should Do
  - If new directories are created or detected. update this AGENTS.md, README.md directory structure section.
  - When asked to "commit this", run `mvn test` (and `mvn checkstyle:check` if a lint target is available) first unless no .java files were changed or if the user says otherwise
  - Prefer using `make` commands for build, test, lint activities over direct mvn or java cli commands (unless testing smaller, single-file changes).
  - Adhere .github/pull_request_template.md structure if asked to create a pull request
  - When told `commit and push` also create a github pull request describing the change.

  ## Allowed without prompt:
  - read files, list files, `ls`
  - all `make` commands and their go equivalents (`mvn test`, `mvn clean`, etc)
  - editing markdown files

  ## Ask first:
  - package installs,
  - git push
  - deleting files, `rm`, chmod

