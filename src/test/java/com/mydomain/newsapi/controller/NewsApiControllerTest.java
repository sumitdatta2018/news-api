package com.mydomain.newsapi.controller;

import com.kwabenaberko.newsapilib.models.Article;
import com.mydomain.newsapi.model.NewsFeedBucket;
import com.mydomain.newsapi.model.SearchCriteria;
import com.mydomain.newsapi.service.NewsApiService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.core.TypeReferences;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AutoConfigureWebTestClient(timeout = "36000")
@ActiveProfiles(profiles = {"dev"})
@WebFluxTest(value = NewsApiController.class)
public class NewsApiControllerTest {

  private static final String PATH = "/v1/news-feed";

  @Autowired
  private WebTestClient webTestClient;

  @MockBean
  private NewsApiService newsApiService;

  protected ResponseSpec doGet(final WebTestClient client) {
    return client.get().uri("/v1/news-feed?searchKeyword=apple").header(ACCEPT, "application/vnd.api+json").exchange()
        .expectStatus().isOk();
  }

  protected WebTestClient.BodyContentSpec doGetAndGet400Error(final WebTestClient client) {
    return client.get().uri(NewsApiControllerTest.PATH).header(CONTENT_TYPE, APPLICATION_JSON_VALUE).exchange()
        .expectStatus().isBadRequest().expectBody();
  }

  protected WebTestClient.BodyContentSpec doGet500Error(final WebTestClient client, final String uri) {
    return client.get().uri(uri).header(CONTENT_TYPE, APPLICATION_JSON_VALUE).exchange().expectStatus()
        .is5xxServerError().expectBody();
  }

  @DisplayName("Get success response for get news feed from News API")
  @Test
  void getNewsApiSuccessResponse() {
    Article article = new Article();
    article.setAuthor("test-author");
    article.setContent("apple");
    article.setDescription("test-description");
    article.setTitle("test-title");
    SearchCriteria searchCriteria = SearchCriteria.builder().searchKeyword("apple").build();
    List<NewsFeedBucket> newsFeedBuckets =
        Collections.singletonList(NewsFeedBucket.builder().id(1).articles(Collections.singletonList(article)).build());
    PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(searchCriteria.getPageSize(),
        searchCriteria.getPageNumber(), newsFeedBuckets.size());
    Link selfLink = linkTo(methodOn(NewsApiController.class).getNewsFeed(searchCriteria)).withSelfRel();

    when(newsApiService.getNewsFeed(searchCriteria))
        .thenReturn(Mono.just(PagedModel.of(newsFeedBuckets, pageMetadata, selfLink)));

    doGet(webTestClient).expectBody(new TypeReferences.PagedModelType<NewsFeedBucket>() {}).consumeWith(result -> {
      PagedModel<NewsFeedBucket> response = result.getResponseBody();
      assertThat(Objects.requireNonNull(response).getRequiredLink(IanaLinkRelations.SELF))
          .isEqualTo(Link.of("/v1/news-feed"));
      assertThat(response.getContent().stream().findFirst().get().getArticles().get(0).getAuthor())
          .isEqualTo("test-author");
      assertThat(response.getContent().stream().findFirst().get().getArticles().get(0).getContent()).isEqualTo("apple");
      assertThat(response.getContent().stream().findFirst().get().getArticles().get(0).getDescription())
          .isEqualTo("test-description");
      assertThat(response.getContent().stream().findFirst().get().getArticles().get(0).getTitle())
          .isEqualTo("test-title");
    });
  }

  @DisplayName("Get error when searchKeyword attribute is missing in query param")
  @Test
  void getNewsApiResponseForMissingParam() {
    doGetAndGet400Error(webTestClient).jsonPath("$").isNotEmpty().jsonPath("$.reason")
        .isEqualTo("NEWS_API_INVALID_INPUT").jsonPath("$.code").isEqualTo(400);
  }
}
