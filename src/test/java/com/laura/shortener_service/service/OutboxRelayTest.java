package com.laura.shortener_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.kafka.support.SendResult;
import tools.jackson.databind.ObjectMapper;
import com.laura.shortener_service.entity.OutboxEvent;
import com.laura.shortener_service.entity.OutboxStatus;
import com.laura.shortener_service.event.LinkClickedEvent;
import com.laura.shortener_service.repository.OutboxEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OutboxRelayTest {

  @Mock
  private OutboxEventRepository outboxRepository;

  @Mock
  private KafkaTemplate<String, LinkClickedEvent> kafkaTemplate;

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private OutboxRelay outboxRelay;

  private OutboxEvent event;
  private LinkClickedEvent linkClickedEvent;

  @BeforeEach
  void setUp() {
     event = new OutboxEvent(
        "LINK",
        "abc123",
        "LINK_CLICKED",
        "{}",
        OutboxStatus.PENDING
     );
     linkClickedEvent = new LinkClickedEvent(
        "abc123",
        "https://example.com",
        LocalDateTime.now(),
        "test-agent",
        "test-correlation-id"
     );
    when(outboxRepository.findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING))
        .thenReturn(List.of(event));

    when(objectMapper.readValue(event.getPayload(), LinkClickedEvent.class)).
        thenReturn(linkClickedEvent);
  }

  @Test
  void relay_whenKafkaAvailable_marksSent() {
    CompletableFuture<SendResult<String, LinkClickedEvent>> future =
        CompletableFuture.completedFuture(null);
    when(kafkaTemplate.send("link-clicks", linkClickedEvent)).thenReturn(future);

    outboxRelay.relay();

    assertEquals(OutboxStatus.SENT, event.getStatus());
  }

  @Test
  void relay_whenKafkaFails_keepsPending() {
    CompletableFuture<SendResult<String, LinkClickedEvent>> failedFuture =
        CompletableFuture.failedFuture(new RuntimeException("Kafka unavailable"));
    when(kafkaTemplate.send("link-clicks", linkClickedEvent)).thenReturn(failedFuture);

    outboxRelay.relay();

    assertEquals(OutboxStatus.PENDING, event.getStatus());

  }
}