# Contribution Guidelines

## Standards for contributing code for review

1.) All changes should be small iterations that are easy to review.  No Pull request or change should be over
2.) All commit names should be useful and should follow the format of: (type) description.  Where type is one of [bugfix, feat, chore, infra, config, refactor] if the commit is 
3.) All pull request comments and reviews should follow conventional comments structure 
4.) Describe and propose any major refactors and ask for approval before making the change.
5.) Makefile commands should always be updated when a change is made to how the project is built, tested, packaged, or versioned. 
6.) The README.md should also be updated  when a change is made to how the project is built, tested, versioned, or when changes to the Makefile have been contributed
7.) The README.md as well as the infra/Dockerfile and infra/docker-compose file should be kept updated when a build-process change is made to ensure all build dependencies are kept in sync with the build container

## Coding Style

1.) All contributions should follow Google's coding style guidelines (as documented here: https://google.github.io/styleguide/javaguide.html )
2.) Code comments provided in source code should describe why the code was written, NOT what the code is doing.  Code comments should be descriptive and not just a copy of the code.
3.) Cyclomatic complexity should be kept to a minimum.  Prefer creating new functions to reduce complex boolean evaluations or code more than 3 tabs deep.
4.) Use streams instead of arrays when mapping or filtering, otherwise use arrays for standard iteration.
5.) Do not use "Manager" or "Service" in any domain classes - choose a better description of what the class is doing or break the class apart until the class methods are more clearly organized.


## API Standards:

1.) All new and existing API endpoints should be compliant with OpenAPIv3 specifications and standards.
2.) All new API endpoints should be versioned and documented with the version number in the path.  For example: /api/v1/endpoint
3.) All existing endpoints should have their version incremented when a backward-incompatible change is introduced (such adding a new required parameter or field, or changing the name of a field)
4.) All protobuf messages should be versioned and documented with the version number in the path.  For example: /api/v1/endpoint
5.) All new methods should come with at least one unit tests and all new features should come with at least one integration tests.  These tests should  cover both an expected (green) test and the failure (red) test


## Contributing New Domains

1.) All new domains should follow the file structure delimited in the structure.md document that is demonstrated through the example "books" folder