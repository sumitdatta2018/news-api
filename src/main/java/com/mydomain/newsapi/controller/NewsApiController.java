package com.mydomain.newsapi.controller;

import com.mydomain.newsapi.model.ErrorResponse;
import com.mydomain.newsapi.model.NewsFeedBucket;
import com.mydomain.newsapi.model.SearchCriteria;
import com.mydomain.newsapi.service.NewsApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static com.mydomain.newsapi.constant.SwaggerConstants.INTERNAL_ERROR;
import static com.mydomain.newsapi.constant.SwaggerConstants.VALIDATION_ERROR;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class NewsApiController {

  private final NewsApiService newsApiService;


  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/v1/news-feed", produces = "application/vnd.api+json")
  @Operation(summary = "Get news feed based on duration and timeunit")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "News Api invoked successfully"),
      @ApiResponse(responseCode = "400", description = "Request validation failed",
          content = {@Content(schema = @Schema(implementation = ErrorResponse.class),
              examples = {@ExampleObject(VALIDATION_ERROR)})}),
      @ApiResponse(responseCode = "500", description = "There was an error retrieving news feed",
          content = {@Content(schema = @Schema(implementation = ErrorResponse.class),
              examples = {@ExampleObject(INTERNAL_ERROR)})})})
  public Mono<PagedModel<NewsFeedBucket>> getNewsFeed(@ParameterObject @Valid SearchCriteria searchCriteria) {
    return newsApiService.getNewsFeed(searchCriteria);
  }
}
