package com.laura.shortener_service.metrics;

import com.laura.shortener_service.entity.OutboxStatus;
import com.laura.shortener_service.repository.OutboxEventRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxMetrics {
  private final MeterRegistry meterRegistry;
  private final OutboxEventRepository outboxEventRepository;

  @PostConstruct
  public void registerMetrics() {
    Gauge.builder(
        "outbox.pending.count",
        outboxEventRepository,
        repository-> repository.countByStatus(OutboxStatus.PENDING)
    ).register(meterRegistry);
  }
}
