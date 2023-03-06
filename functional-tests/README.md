## News API functional tests

### Test stack

- Java
- Cucumber
- Rest Assured
- AssertJ
- Gradle
- Docker

## Run the tests

The tests can be run in several ways:

### Gradle wrapper command line

```shell
cd {path to project}
./gradlew runFunctionalTestsLocal
```
This will use docker compose to mock the endpoints and in CI pipeline this job is already configured

### Configuring the tests in integrated environment

These tests will be running in integrated environment after successful deployment.
To be able to run the tests on the environments the environment variable `NEWS_API_BASE` needs to be updated.

### Reports

- Functional Test report can be found in: \functional-tests\build\cucumber-reports\cucumber_results.html