package com.mydomain.newsapi.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.jackson.Jacksonized;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kwabenaberko.newsapilib.models.Article;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@Jacksonized
@EqualsAndHashCode(callSuper = false)
@JsonInclude(Include.NON_NULL)
public class NewsFeed extends RepresentationModel {

  private Integer totalResults;
  private Integer totalNumberOfBuckets;
  private List<NewsFeedBucket> newsFeedBuckets;
}
