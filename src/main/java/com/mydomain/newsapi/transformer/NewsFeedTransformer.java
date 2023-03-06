package com.mydomain.newsapi.transformer;

import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.mydomain.newsapi.constant.ExceptionMessageConstants;
import com.mydomain.newsapi.controller.NewsApiController;
import com.mydomain.newsapi.exception.NewsApiException;
import com.mydomain.newsapi.model.NewsFeedBucket;
import com.mydomain.newsapi.model.SearchCriteria;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NewsFeedTransformer {
  private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

  public static PagedModel<NewsFeedBucket> buildNewsFeed(ArticleResponse articleResponse,
      SearchCriteria searchCriteria) {
    final var groupedNewsFeeds = articleResponse.getArticles().stream().collect(Collectors.groupingBy(a -> {
      final var publishedDate = LocalDateTime.parse(a.getPublishedAt(), dateTimeFormatter);
      return buildNewsFeedBucketGroupIndex(publishedDate, searchCriteria);
    }));
    return buildJsonApiResponse(populateNewsFeedBucket(groupedNewsFeeds), searchCriteria);
  }

  private static PagedModel<NewsFeedBucket> buildJsonApiResponse(List<NewsFeedBucket> newsFeedBuckets,
      SearchCriteria searchCriteria) {
    final var pageMetadata =
        new PageMetadata(searchCriteria.getPageSize(), searchCriteria.getPageNumber(), newsFeedBuckets.size());
    final var selfLink = linkTo(methodOn(NewsApiController.class).getNewsFeed(searchCriteria)).withSelfRel();
    return PagedModel.of(newsFeedBuckets, pageMetadata, selfLink);
  }

  @SneakyThrows
  private static Integer buildNewsFeedBucketGroupIndex(LocalDateTime publishedDate, SearchCriteria searchCriteria) {
    final var groupingDuration = searchCriteria.getGroupingDuration();
    return switch (searchCriteria.getGroupingInterval()) {
      case MINUTES -> publishedDate.get(ChronoField.MINUTE_OF_DAY) / groupingDuration;
      case HOURS -> publishedDate.get(ChronoField.HOUR_OF_DAY) / groupingDuration;
      case DAYS -> publishedDate.get(ChronoField.DAY_OF_YEAR) / groupingDuration;
      case WEEKS -> publishedDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR) / groupingDuration;
      case MONTHS -> publishedDate.get(ChronoField.MONTH_OF_YEAR) / groupingDuration;
      case YEARS -> publishedDate.get(ChronoField.YEAR) / groupingDuration;
      default -> throw new NewsApiException(ExceptionMessageConstants.UNSUPPORTED_INTERVAL_UNIT.getValue());
    };
  }

  private static List<NewsFeedBucket> populateNewsFeedBucket(Map<Integer, List<Article>> newsFeeds) {
    return newsFeeds.entrySet().stream().map(newFeed -> NewsFeedBucket.builder().id(newFeed.getKey())
        .articles(newFeed.getValue()).articleCount(newFeed.getValue().size()).build()).toList();
  }

}
