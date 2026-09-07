package com.laura.shortener_service.client;

import com.laura.shortener_service.dto.AnalyticsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsClientTest {
  private static final String SHORT_CODE = "a7f3c9d2";

  @Mock
  private WebClient.Builder builder;

  @Mock
  private WebClient webClient;

  @Mock
  private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

  @Mock
  private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

  private AnalyticsClient analyticsClient;

  @BeforeEach
  void setUp() {
    when(builder.baseUrl(anyString())).thenReturn(builder);
    when(builder.build()).thenReturn(webClient);

    analyticsClient = new AnalyticsClient(
        builder,
        "http://localhost:8081"
    );
  }

  @Test
  void getAnalytics_whenServiceUnavailable_returnsDefaultResponse() {
    doReturn(requestHeadersUriSpec).when(webClient).get();

    doReturn(requestHeadersSpec)
        .when(requestHeadersUriSpec)
        .uri("/api/analytics/{shortCode}", SHORT_CODE);

    when(requestHeadersSpec.retrieve())
        .thenThrow(new RuntimeException("analytics-service unavailable"));

    AnalyticsResponse response = analyticsClient.getAnalyticsByShortCode(SHORT_CODE);

    assertEquals(SHORT_CODE, response.shortCode());
    assertEquals(0L, response.totalClicks());
  }
}