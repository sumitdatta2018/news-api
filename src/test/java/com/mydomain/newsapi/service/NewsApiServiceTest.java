package com.mydomain.newsapi.service;


import com.mydomain.newsapi.mock.NewsApiMock;
import com.mydomain.newsapi.model.SearchCriteria;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;


import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({MockitoExtension.class})
public class NewsApiServiceTest {

  private static final String NEWS_API_TO_GET_NEWS_FEED_BY_KEYWORD = "mock/";

  @RegisterExtension
  private static final NewsApiMock NEWS_API_MOCK = new NewsApiMock();

  private NewsApiService newsApiService;

  @BeforeEach
  @SneakyThrows
  public void setup() {
    var newsApiWebClient = NEWS_API_MOCK.buildNewsApiWebClient();
    newsApiService = new NewsApiService(newsApiWebClient);
  }

  @DisplayName("given searchKeyword then return success from News API")
  @Test
  void getNewsAPiSuccessResponse() {

    NEWS_API_MOCK
        .stubNewsApiToGetNewsFeedByKeyword(NEWS_API_TO_GET_NEWS_FEED_BY_KEYWORD + "everything-news-api-response.json");
    SearchCriteria searchCriteria = SearchCriteria.builder().build();
    StepVerifier.create(newsApiService.getNewsFeed(searchCriteria)).assertNext(
        model -> assertThat(model.getContent().stream().findFirst().get().getArticles().stream().findFirst().get())
            .hasFieldOrPropertyWithValue("author", "Scott Gilbertson")
            .hasFieldOrPropertyWithValue("title", "14 Best Deals: Apple iPads, Laptops, and Outdoor Gear")
            .hasFieldOrPropertyWithValue("description",
                "Whether you’re looking for new tech to get through long cool nights, or planning a spring camping trip, there are plenty of discounts that can help.")
            .hasFieldOrPropertyWithValue("url", "https://www.wired.com/story/midweek-deals-022223/")
            .hasFieldOrPropertyWithValue("content",
                "Depending on where you live, it's either starting to look a little like spring or still totally socked in winter. Damn that groundhog. Either way, we have some deals for you. Those of you stuck in th… [+5960 chars]")
            .hasFieldOrPropertyWithValue("urlToImage",
                "https://media.wired.com/photos/627af5ce194f8820f344ac62/191:100/w_1280,c_limit/Pixel-6a-SOURCE-Google-Gear.jpg")
            .hasFieldOrPropertyWithValue("publishedAt", "2023-02-23T12:00:00Z"))
        .expectComplete().verify();
  }
}
