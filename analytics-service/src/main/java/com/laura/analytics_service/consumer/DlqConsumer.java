package com.laura.analytics_service.consumer;

import com.laura.analytics_service.event.LinkClickedEvent;
import com.laura.analytics_service.service.FailedEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DlqConsumer {
  private final FailedEventService failedEventService;

  @KafkaListener(topics = "link-clicks-dead-letter", groupId = "analytics-dlq")
  public void consume(LinkClickedEvent event){
    try{
      MDC.put("correlationId", event.correlationId());
      log.error("Failed event for {}",  event);
      failedEventService.add(event);
    }
    finally {
      MDC.clear();
    }
  }
}
