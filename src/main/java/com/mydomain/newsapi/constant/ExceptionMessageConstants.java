package com.mydomain.newsapi.constant;

public enum ExceptionMessageConstants {
  UNSUPPORTED_INTERVAL_UNIT("The provided interval unit is not supported in the API"), NEWS_API_INVALID_INPUT("");

  private String value;

  public String getValue() {
    return value;
  }

  ExceptionMessageConstants(String value) {
    this.value = value;
  }

}
