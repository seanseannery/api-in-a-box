# Include setup makefile
include infra/local-setup.mk

.PHONY: build run all docker-build docker-run docker-stop attach-docker docker-logs

# Default port
PORT ?= 8080

# Default target
all: build run

# Build the project
build:
	@echo "Building the project..."
	@mvn protobuf:generate
	@mvn clean package

# Build Docker image
docker-build:
	@echo "Building Docker image..."
	@docker-compose -f infra/docker-compose.yml build

# Run the application using Docker
run: docker-build
	@echo "Running the application in Docker..."
	@PORT=$(PORT) docker-compose -f infra/docker-compose.yml up -d

# Show logs
docker-logs:
	@docker-compose -f infra/docker-compose.yml logs -f

open-swagger:
	@echo "Opening Swagger UI in browser..."
	@open http://localhost:$(PORT)/swagger-ui.html

# Attach to the running container's shell
docker-shell:
	@echo "Attaching to container shell..."
	@docker exec -it $$(docker-compose -f infra/docker-compose.yml ps -q api) /bin/bash

# Stop the Docker containers
docker-stop:
	@echo "Stopping Docker containers..."
	@docker-compose -f infra/docker-compose.yml down

# Help command
# see infra/local-setup.mk for setup command implementation and code
help:
	@echo "Available commands:"
	@echo "  make setup         - Setup development environment (macOS)"
	@echo "  make build         - Build the project"
	@echo "  make docker-build  - Build Docker image"
	@echo "  make run           - Build and run the application in Docker (8080)"
	@echo "  make docker-logs   - Show container logs"
	@echo "  make open-swagger  - Open Swagger UI in browser"
	@echo "  make docker-shell  - Attach to the running container's shell"
	@echo "  make docker-stop   - Stop Docker containers"
	@echo "  make all           - Clean, build and run the application (default)"
	@echo "  make help          - Show this help message" 
