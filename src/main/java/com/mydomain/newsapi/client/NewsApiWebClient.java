package com.mydomain.newsapi.client;

import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@NoArgsConstructor
@AllArgsConstructor
public class NewsApiWebClient {
  @Value("${newsApi.authToken}")
  private String apiKey;

  @Value("${newsApi.baseurl}")
  private String apiBaseUrl;

  private static final String API_CONTEXT = "/v2/everything";

  private HttpClient getHttpClient() {
    return HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 50000)
        .responseTimeout(Duration.ofMillis(50000))
        .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(50000, TimeUnit.MILLISECONDS))
            .addHandlerLast(new WriteTimeoutHandler(50000, TimeUnit.MILLISECONDS)));
  }

  private WebClient getWebClient() {
    return WebClient.builder().baseUrl(apiBaseUrl).clientConnector(new ReactorClientHttpConnector(getHttpClient()))
        .build();
  }

  public Mono<ArticleResponse> getNewsFeed(String searchKeyword, String from, String to) {
    return getWebClient().get()
        .uri(builder -> builder.path(API_CONTEXT).queryParam("apiKey", apiKey).queryParam("q", searchKeyword).build())
        .retrieve().bodyToMono(ArticleResponse.class);

  }
}
