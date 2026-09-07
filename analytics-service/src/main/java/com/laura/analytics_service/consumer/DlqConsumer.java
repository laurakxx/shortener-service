package com.laura.analytics_service.consumer;

import com.laura.analytics_service.event.LinkClickedEvent;
import com.laura.analytics_service.service.FailedEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DlqConsumer {
  private final FailedEventService failedEventService;

  @KafkaListener(topics = "link-clicks-dead-letter", groupId = "analytics-dlq")
  public void consume(LinkClickedEvent event){
    System.out.println("Failed event: " + event);
    failedEventService.add(event);
  }
}
