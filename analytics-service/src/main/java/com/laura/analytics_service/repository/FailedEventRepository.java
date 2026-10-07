package com.laura.analytics_service.repository;

import com.laura.analytics_service.entity.FailedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FailedEventRepository extends JpaRepository<FailedEvent, Long> {
}
