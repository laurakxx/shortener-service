package com.laura.shortener_service.client;


import com.laura.shortener_service.dto.AnalyticsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

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
          .block();
    }
    catch (Exception e){
      return new AnalyticsResponse(shortCode, 0L);
    }

  }
  
}
