
.PHONY: build run all docker-build docker-run docker-stop docker-shell docker-logs open-swagger lint test setup-local-dev help

# Default port
PORT ?= 8080

# Default target
all: build run ## Clean, build and run the application (default)

build: ## Build the project (mvn clean package)
	@echo "Building the project..."
	@mvn clean && mvn package

test: ## Run all unit and integration tests
	@mvn clean && mvn test

lint: ## Auto-format Java sources with google-java-format then validate with checkstyle
	@echo "Formatting Java sources..."
	@find src -name "*.java" | xargs google-java-format --replace
	@echo "Running checkstyle..."
    @mvn checkstyle:check

docker-build: ## Build package and the associated Docker image
	@echo "Building Docker image..."
	@docker-compose -f infra/docker-compose.yml build

run: docker-build ## Build and run the application in Docker on $(PORT)
	@echo "Running the application in Docker..."
	@PORT=$(PORT) docker-compose -f infra/docker-compose.yml up -d
	@sleep 5
	$(MAKE) open-swagger
	$(MAKE) docker-logs

docker-logs: ## Show container logs
	@docker-compose -f infra/docker-compose.yml logs -f

open-swagger: ## Open Swagger UI in browser
	@echo "Opening Swagger UI in browser..."
	@open http://localhost:$(PORT)/swagger-ui.html

docker-shell: ## Attach to the running container's shell
	@echo "Attaching to container shell..."
	@docker exec -it $$(docker-compose -f infra/docker-compose.yml ps -q api) /bin/bash

docker-stop: ## Stop Docker containers
	@echo "Stopping Docker containers..."
	@docker-compose -f infra/docker-compose.yml down

# Include setup makefile
include infra/local-setup.mk

help: ## Show this help message
	@echo "Available commands:"
	@grep -hE '^[a-zA-Z_-]+:.*## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*## "}; {printf "  make %-20s - %s\n", $$1, $$2}'
