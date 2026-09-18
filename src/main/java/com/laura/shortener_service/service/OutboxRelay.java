package com.laura.shortener_service.service;

import com.laura.shortener_service.entity.OutboxEvent;
import com.laura.shortener_service.entity.OutboxStatus;
import com.laura.shortener_service.event.LinkClickedEvent;
import com.laura.shortener_service.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class OutboxRelay {
  private final OutboxEventRepository outboxEventRepository;
  private final KafkaTemplate<String, LinkClickedEvent> kafkaTemplate;
  private final ObjectMapper objectMapper;
  private static final String LINK_CLICKS_TOPIC = "link-clicks";

  @Scheduled(fixedDelay = 5000)
  public void relay() {
    List<OutboxEvent> list = outboxEventRepository.findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING);
    if(list.isEmpty()) {
      return;
    }
    for (OutboxEvent event: list) {
      try{
        LinkClickedEvent linkClickedEvent = objectMapper.readValue(event.getPayload(), LinkClickedEvent.class);
        kafkaTemplate.send(LINK_CLICKS_TOPIC, linkClickedEvent).get();
        event.setStatus(OutboxStatus.SENT);
        event.setSentAt(LocalDateTime.now());
        outboxEventRepository.save(event);
      }
      catch (Exception e) {
        log.error("Failed to send outbox event {}", event.getId(), e);
      }

    }
  }
}
