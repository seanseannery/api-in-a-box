# Contributing changes to `api-in-a-box`

  Thanks for your interest in `api-in-a-box`! Contributions are welcome — whether it's a bug fix, a new feature, or just a typo correction. Feel free to submit pull requests with details on the fix or just submit an issue ticket detailing the feature request or bug fix. The #1 rule of being a positive contributor to this project is dont be an asshole (non-constructive or non-inclusive negative comments, pushing half-ass code without bothering to follow guidelines, making unnecessary work)

---

# Getting Started

### Setting up your Development Environment

```bash
make setup
```
This will install required dependencies for local development on mac

### Building and Running Tests

All common build and test functionality should be provided out of the box with `make` command as defined in `./Makefile`

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


# Contributing and Code Standards

  Below describes the style guidelines and proper process for making changes in this repo. We use a standard trunk-based development flow 
  1. make a new feature branch
  2. implement, test and lint your changes 
  3. commit working, tested code to your feature branch and push it to github
  4. create PR against main
  5. if pr test automation passes and the code follows style/formatting, an admin will merge it into `main` branch)
  6. ?
  7. Profit

## Java Code and Style Guidelines

### Style and Formatting

- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) to the best of your effort
- APIs and DTOs should be versioned. Initial development uses V1; increment the version for backward-incompatible changes.
- K.I.S.S - readability over micro-optimization: clear code is more important than saving microseconds
- Keep functions focused; if a function needs a comment to explain what it does, consider splitting it
- Code comments should describe **why** the code was written, not **what** it is doing — the code itself explains the what
- Keep cyclomatic complexity low: prefer extracting new functions over deeply nested conditionals or code more than 3 levels deep
- Do not use "Manager" or "Service" in domain class names — choose a name that clearly describes what the class does, or break it apart until the methods are more cohesive
- Use streams when mapping or filtering collections; use standard loops for straightforward iteration
- Run `mvn checkstyle:check` before committing — CI will catch unformatted code
- Always write automated tests for your contribution. While there is no explicit coverage goal, test coverage should never decrease.

### Testing Best Practices

**Use JUnit 5** (not JUnit 4) for all new tests:

**Key testing guidelines:**
- Use Mockito for mocking; consider package-private methods for testability
- Mock services when possible for faster unit tests
- All new methods must have at least one unit test covering the happy path and at least one covering the failure case
- All new features must have at least one integration test in addition to unit tests

### Dependencies

- Prefer standard library functions and utilities over reinventing the wheel yourself, the standard library is extensive.
- Only use external dependencies if it improves code simplicity/security and has a very active community, otherwise prefer the standard library
- If a dependency is necessary, discuss it in the PR description and ensure it is well-maintained
- Dependency versions must be explicitly declared in `pom.xml`; use the most recent version compatible with Java 24 and existing dependencies

### API Standards

- All new and existing API endpoints must comply with [OpenAPI v3](https://spec.openapis.org/oas/v3.0.0) specifications
- All new API endpoints must be versioned in the path: `/api/v1/endpoint`
- Increment the version on any endpoint when a backward-incompatible change is introduced (e.g. adding a required field, renaming a field)
- All Protocol Buffer messages must be versioned consistently with their REST endpoint version

### Build and Documentation Sync

- Update `Makefile` whenever a change is made to how the project is built, tested, packaged, or versioned
- Update `README.md` whenever the Makefile changes or when the build/test/run process changes
- Keep `infra/Dockerfile` and `infra/docker-compose.yml` in sync with all build-time dependencies

### Contributing New Domains

New domain modules must follow the folder structure defined in `AGENTS.md`:
- `api/` — Java API interface + `dto/` subfolder for `.proto` files
- `controller/` — REST controller implementing the interface
- `model/` — domain model + mapper
- `repository/` — data access layer


## Contributing your changes

Pull requests and changes that don't follow the below standards have an extremely likely chance of failing or getting rejected. The installed git hooks will help remind you before you push changes. The github actions in the repo will re-verify and must pass in order to be merged.

### Creating Feature Branches

- Feature branches must be created to stage all of your changes and issue a pull request against main.
- Feature branches must be refreshed using `git pull` to ensure they are up to date and any conflicts are resolved before pushing them to github
- Feature branches must follow the below naming convention:
  - only lowercase alpha-numeric characters, no punctuation outside of hyphen and slash
  - references an issue/ticket id if one exists, otherwise is descriptive and consise (no more than 4 words)
  - prefixes Follow [Conventional Commits](https://www.conventionalcommits.org/):
  	- ex. `feat/add-flag-support`, `fix/resolve-issue-123`, `ci/add-test-job`, `infra/add-terraform`, `docs/fix-typo-ticket1234`)

### Commit Messages

Follow [Conventional Commits](https://www.conventionalcommits.org/):
```
<type>(<optional scope>): <short summary> (issueID)
```
Common types: `feat`, `fix`, `refactor`, `test`, `docs`, `chore`, `ci`, `infra`, `config`
Examples:
```
fix: handle missing Opsfile gracefully (Issue#42)
feat(parser): support multi-line variable values
docs: add contributing guide
```

### Pull Requests

- One logical change per PR — keep diffs focused and prefer small, reviewable PRs over large ones
- PR title should follow the same Conventional Commits format as the commit message
- PR description contents should Include a short description of *why* the change is needed, not just what it does as well as a list of any new dependecies added and tests added.
- Ensure `mvn test` and `mvn checkstyle:check` pass locally before opening a PR or CI automation will auto-reject your PR.

# Code Review Feedback & Community Participation

The #1 rule of being a positive contributor to this project is dont be an asshole (non-constructive or non-inclusive negative comments, pushing half-ass code without bothering to follow guidelines, making unnecessary work).  Breaking this rule will get your account banned.

## Providing PR Feedback / Comments

Reviewers use [Conventional Comments](https://conventionalcomments.org/) to signal intent and provide constructive feedback. Commenters should assume good intent and remember that everyone is at different places in their coding/engineering life-journey:

| Label | Meaning |
|-------|---------|
| `suggestion:` | Optional improvement, not blocking |
| `issue:` | Must be addressed before merge |
| `question:` | Clarification needed, not blocking |
| `nit:` | Minor style/preference, non-blocking |
| `praise:` | Positive callout |

---








