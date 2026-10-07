package com.laura.analytics_service.repository;

import com.laura.analytics_service.entity.ClickEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {

  long countByShortCode(String shortCode);

  @Modifying
  @Query(value = """
      INSERT INTO click_events (
          event_id,
          short_code,
          original_url,
          clicked_at,
          user_agent,
          correlation_id
      )
      VALUES (
          :eventId,
          :shortCode,
          :originalUrl,
          :clickedAt,
          :userAgent,
          :correlationId
      )
      ON CONFLICT (event_id) DO NOTHING
      """, nativeQuery = true)
  int insertClickIfNotExists(
      @Param("eventId") String eventId,
      @Param("shortCode") String shortCode,
      @Param("originalUrl") String originalUrl,
      @Param("clickedAt") LocalDateTime clickedAt,
      @Param("userAgent") String userAgent,
      @Param("correlationId") String correlationId
  );
}