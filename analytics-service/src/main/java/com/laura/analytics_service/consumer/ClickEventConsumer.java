package com.laura.analytics_service.consumer;

import com.laura.analytics_service.event.LinkClickedEvent;
import com.laura.analytics_service.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClickEventConsumer {
  private final AnalyticsService analyticsService;

  @KafkaListener(topics = "link-clicks", groupId = "analytics-service")
  public void consume(LinkClickedEvent event) {
    System.out.println("Click received: " + event);
    analyticsService.recordClick(event);
  }
}
