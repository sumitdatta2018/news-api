package com.mydomain.newsapi.handler;


import com.mydomain.newsapi.constant.ExceptionMessageConstants;
import com.mydomain.newsapi.controller.NewsApiController;
import com.mydomain.newsapi.model.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebInputException;

import static org.apache.logging.log4j.message.ParameterizedMessage.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice(assignableTypes = {NewsApiController.class})
public class NewsApiExceptionHandler {

  public static final String INVALID_PROPERTY_DESC = "Validation failed for {} - {}";

  @ExceptionHandler(BindException.class)
  public ResponseEntity<ErrorResponse> handleWebExchangeBindException(final BindException exchangeBindException) {

    final var errorDesc = exchangeBindException.getBindingResult().getAllErrors().stream()
        .map(e -> format(INVALID_PROPERTY_DESC, new Object[] {((FieldError) e).getField(), e.getDefaultMessage()}))
        .toList();
    return ResponseEntity.status(BAD_REQUEST).body(ErrorResponse.builder().code(BAD_REQUEST.value())
        .reason(ExceptionMessageConstants.NEWS_API_INVALID_INPUT.name()).details(String.join(", ", errorDesc)).build());
  }

  @ExceptionHandler(ServerWebInputException.class)
  public ResponseEntity<ErrorResponse> handleServerWebInputException(
      final ServerWebInputException serverWebInputException) {

    final var message = serverWebInputException.getMostSpecificCause().getMessage();
    return ResponseEntity.status(BAD_REQUEST).body(ErrorResponse.builder().code(BAD_REQUEST.value())
        .reason(ExceptionMessageConstants.NEWS_API_INVALID_INPUT.name()).details(message).build());
  }
}
