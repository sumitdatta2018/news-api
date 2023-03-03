# NEWS API
This repository is to expose news feed from News API everything endpoint.

## Build
Use './gradlew clean build' command to build the code with junit test, spotless formatter

## Run the Service
Use './gradlew bootRun' command to run the application.

## Test reports
- Coverage report can be found in: \build\reports\jacoco\test\html\index.html
- Unit test report can be found in: \build\reports\tests\test\index.html

## API 
### Tech stack

- Java
- Springboot
- Spring Reactive Webflux
- OpenAPI
- JSON API Spec
- Gradle
- Docker

### API Design and components

- For API development, Top-down approach with TDD is followed. 
- Unit test for individual modules can be found inside 'test'.
- JsonApi Spec is used for standard data response with Hypermedia


## Functional tests
