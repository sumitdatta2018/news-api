package com.mydomain.newsapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class NewsApiException extends RuntimeException {

  private final String reason;

  public NewsApiException(String reason, Throwable exc) {
    super(exc);
    this.reason = reason;
  }
}
