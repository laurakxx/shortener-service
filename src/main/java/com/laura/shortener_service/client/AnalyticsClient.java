package com.laura.shortener_service.client;


import com.laura.shortener_service.dto.AnalyticsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.Duration;

@Slf4j
@Component
public class AnalyticsClient {
  private final WebClient webClient;

  public AnalyticsClient(WebClient.Builder builder, @Value("${analytics.base-url}") String baseUrl) {
    this.webClient = builder.baseUrl(baseUrl).build();
  }
  public AnalyticsResponse getAnalyticsByShortCode(String shortCode) {
    try {
      return webClient
          .get().uri("/api/analytics/{shortCode}", shortCode)
          .retrieve()
          .bodyToMono(AnalyticsResponse.class)
          .timeout(Duration.ofSeconds(2))
          .block();
    }
    catch (Exception e){
      log.warn("Analytics service failed for shortCode={}", shortCode, e);
      return new AnalyticsResponse(shortCode, 0L);
    }

  }
  
}
