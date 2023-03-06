Feature: Get News feed from News API

  Background:
    Given the news api mock is reset

  @integration-test
  Scenario Outline: Check News API Actuator End point
    When a GET request is sent to the News Api "/actuator/health" endpoint
    Then the response code will be <code>
    And the json response body should match to "newsApi/response/<resFile>"
    Examples: 
      | resFile                | code |
      | actuator-response.json |  200 |
  @integration-test
  Scenario Outline: GET News feed from everything API
    Given the News API mock GET "/v2/everything" endpoint returns data from file "newsApi/mock/response/<mockResponseFile>"
    When a GET request is sent to the News Api "/v1/news-feed?searchKeyword=apple" endpoint
    Then the response code will be <code>
    And the json response body should match to "newsApi/response/<responseFile>"
    Examples:
      | responseFile               | mockResponseFile                  | code |
      | news-api-response.json     | everything-api-mock-response.json |  200 |
