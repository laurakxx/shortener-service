package com.laura.analytics_service.consumer;

import com.laura.analytics_service.event.LinkClickedEvent;
import com.laura.analytics_service.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClickEventConsumer {
  private final AnalyticsService analyticsService;

  @KafkaListener(topics = "link-clicks", groupId = "analytics-service")
  public void consume(LinkClickedEvent event) {
    try {
      MDC.put("correlationId", event.correlationId());

      log.info("Click received for {}", event.shortCode());
      analyticsService.recordClick(event);
    }
    finally{
      MDC.clear();
    }
  }
}
