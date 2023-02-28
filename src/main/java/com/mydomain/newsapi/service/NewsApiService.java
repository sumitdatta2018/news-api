package com.mydomain.newsapi.service;

import com.mydomain.newsapi.client.NewsApiWebClient;
import com.mydomain.newsapi.model.NewsFeedBucket;
import com.mydomain.newsapi.model.SearchCriteria;
import com.mydomain.newsapi.transformer.NewsFeedTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class NewsApiService {

  private final NewsApiWebClient newsApiWebClient;

  public Mono<PagedModel<NewsFeedBucket>> getNewsFeed(SearchCriteria searchCriteria) {
    DateTimeFormatter isoLocalDateTime = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    var newsFeed = newsApiWebClient.getNewsFeed(
        searchCriteria.getSearchKeyword(), isoLocalDateTime.format(LocalDateTime.now(ZoneOffset.UTC)
            .minus(searchCriteria.getGroupingDuration(), searchCriteria.getGroupingInterval())),
        isoLocalDateTime.format(LocalDateTime.now()));
    return newsFeed.map(articleResponse -> NewsFeedTransformer.buildNewsFeed(articleResponse, searchCriteria));
  }
}
