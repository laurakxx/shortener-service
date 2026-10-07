package com.laura.analytics_service.consumer;

import com.laura.analytics_service.event.LinkClickedEvent;
import com.laura.analytics_service.service.FailedEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DlqConsumer {
  private final FailedEventService failedEventService;

  @KafkaListener(topics = "link-clicks-dead-letter", groupId = "analytics-dlq",
      containerFactory = "dlqKafkaListenerContainerFactory")
  public void consume(ConsumerRecord<String, byte[]> record){
    try{
      String recordId = record.topic() + "-" + record.partition() + "-" + record.offset();

      MDC.put("recordId", recordId);
      log.error("Failed Kafka event stored. topic={}, partition={}, offset={}",
          record.topic(),
          record.partition(),
          record.offset()
      );

      failedEventService.save(
          record.topic(),
          record.partition(),
          record.offset(),
          record.value(),
          "Message moved to dead-letter topic");
    }
    finally {
      MDC.clear();
    }
  }
}
