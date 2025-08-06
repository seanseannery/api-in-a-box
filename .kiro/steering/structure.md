# Project and File Structure


The file structure of this project should follow these core architectural approaches in priority order:
1.) Domain Driven Design - all business, model, and presentation logic of a domain should be collocated in one folder / package
2.) Model View Controller - The business logic is separated from data access/model layer and the presentation layer (in this case API request, and response structures)
3.) Follow standard Maven and Spring boot conventions (all source code should be in the src folder, and all test code in the test folder, all resources in the resources folder)
4.) All infrastructure related code should be in the /infra folder - this includes Dockerfile, deployment scripts, and local setup scripts

## Folder Structure

```
├── infra/                # Infrastructure deployment and local setup scripts
│   ├── Dockerfile        # Container build configuration
├── src/main/java/com/apiinabox/
│   ├── account/          # Top level account Domain folder / package
│   │   ├── api/          # API interfaces and Protocol Buffer DTOs
│   │   ├── controller/   # REST controllers and services
│   │   ├── model/        # Domain models and mappers
│   │   └── repository/   # Data access layer (mock implementation)
│   ├── book/             # Top level account book folder / package
│   ├── config/           # Application configuration classes
│   ├── security/         # Security configuration and JWT handling
│   └── common/           # And common utils or logic that is shared across the different domain
├── src/main/resources/   # Application properties and configuration
├── target/               # Maven build output
├── Makefile              # Developer workflow commands
├── pom.xml               # Maven project configuration
└── test-api.sh           # API testing script
```