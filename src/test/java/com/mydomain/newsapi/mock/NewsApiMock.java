package com.mydomain.newsapi.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.mydomain.newsapi.TestUtil;
import com.mydomain.newsapi.client.NewsApiWebClient;
import com.mydomain.newsapi.utils.RedisUtil;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class NewsApiMock extends WireMockExtension {

  private static final int NEWS_API_PORT = 8080;
  private static final String API_KEY = "test-key";
  private static final String API_BASE_URL = "http://localhost:8080";
  private static final String NEWS_API_PATH_REGEX = "/v2/everything";

  public NewsApiMock() {
    super(WireMockExtension.newInstance().options(wireMockConfig().port(NEWS_API_PORT)));
  }

  public NewsApiWebClient buildNewsApiWebClient() {
    return new NewsApiWebClient(API_KEY, API_BASE_URL, buildMockRedisUtil());
  }

  private RedisUtil buildMockRedisUtil() {
    return new RedisUtil(buildMockRedisTemplate(), new ObjectMapper());
  }

  RedisTemplate<String, Object> buildMockRedisTemplate() {
    RedisTemplate<String, Object> template = mock(RedisTemplate.class);
    RedisConnectionFactory connectionFactory = mock(RedisConnectionFactory.class);
    RedisConnection connection = mock(RedisConnection.class);
    when(template.opsForValue()).thenReturn(mock(ValueOperations.class));
    return template;
  }

  public void stubNewsApiToGetNewsFeedByKeyword(final String responsePath) {
    this.stubFor(get(urlPathMatching(NEWS_API_PATH_REGEX)).willReturn(aResponse().withStatus(OK.value())
        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE).withBody(TestUtil.readResponseFile(responsePath))));
  }



}
