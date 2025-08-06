# Product Overview

This project implements an example backend REST API service that can be copied as a template for engineers to spin up new services quickly, the template should accelerate the creation of new backend services at scale. It takes an opinionated approach at implementing common functionality for large scale including: configuration, rate limiting, circuit breaking, logging, authorization and metrics and container infrastructure using popular libraries and technologies. It implements these so that the engineer spinning up their service can focus on business logic not common repeatable live operational best practices.

# Goals

- Enable software engineers to quickly spin up a basic service in less than a day
- Services spun up from this will have common enterprise-scaling and testing features out of the box with minor configuration
- Engineers who use this to spin up a service can focus all their time on business logic instead of operational resilience
- Business logic for a domain is colocated and easily discoverable for faster business logic development.
- Provide an example domain model and service layer that can be used as a template to accelerate creation of other domain logic

# Customers / User Personas

The primary customer that will be using this service is back-end software engineers familiar with Spring Boot, REST APIs, and Cloud infrastructure. Because of that, it needs to be well documented and follow standard software development conventions for large scale services but does not need overly simplistic or non-technical explinations.  This repo / product will only be used by technical software engineers.


# Enterprise scaling and resilience features out-of-the box
- Ratelimiting
    - API Request Rate limiting
    - IP Address Rate limiting
    - ClientID Rate limiting
- Circuit breaking
- Logging
- Metrics
- Health check API
- Operational Monitoring Dashboards

# Enterprise service management features out-of-the box
- Infrastructure-as-code
    - Cloud provisioning with terraform
    - Container infrastructure and orchestration with k8s (maybe)
- Authorization
- Configuration
    - Secret Management
    - Live configuration changes without service restart
- Java Service Client generation from OpenAPI / Swagger spec and Spring Boot
- Swagger documentation