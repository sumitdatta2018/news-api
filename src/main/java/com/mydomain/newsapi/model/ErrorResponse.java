package com.mydomain.newsapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@JsonInclude(Include.NON_NULL)
public class ErrorResponse {

  private final String details;
  private final String reason;
  private final int code;
  private final String errorMessage;
}
