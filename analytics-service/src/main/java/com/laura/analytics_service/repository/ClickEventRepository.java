package com.laura.analytics_service.repository;

import com.laura.analytics_service.entity.ClickEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {
  boolean existsByCorrelationId(String correlationId);
  long countByShortCode(String shortCode);
}
