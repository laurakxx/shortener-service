package com.laura.shortener_service.repository;

import com.laura.shortener_service.entity.OutboxEvent;
import com.laura.shortener_service.entity.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {
  List<OutboxEvent> findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus status);
  long countByStatus(OutboxStatus status);
}
