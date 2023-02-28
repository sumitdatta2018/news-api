package com.mydomain.newsapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kwabenaberko.newsapilib.models.Article;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@Builder
@Jacksonized
@JsonInclude(Include.NON_NULL)
public class NewsFeedBucket extends RepresentationModel<NewsFeedBucket> {
  private Integer id;
  private List<Article> articles;
  private Integer articleCount;
}
