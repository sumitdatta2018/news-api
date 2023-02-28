package com.mydomain.newsapi.constant;

public class SwaggerConstants {

  public static final String VALIDATION_ERROR = """
      {
              "reason": "NEWS_API_INVALID_INPUT",
              "details": "Validation failed for as searchKeyword - should not be empty",
              "code": 400
      }
      """;

  public static final String INTERNAL_ERROR = """
       {
              "reason": "UNABLE_TO_ACCESS_EXTERNAL_API",
              "details": "Exception occurred at News API Endpoint",
              "code": 500
      }
      """;

  private SwaggerConstants() {}
}
