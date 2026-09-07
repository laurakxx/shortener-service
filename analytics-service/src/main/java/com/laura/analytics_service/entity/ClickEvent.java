package com.laura.analytics_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "click_events")
public class ClickEvent {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;
  @Column(name = "short_code", nullable = false)
  String shortCode;
  @Column(name = "original_url", nullable = false)
  String originalUrl;
  @Column(name = "clicked_at", nullable = false)
  LocalDateTime clickedAt;
  @Column(name = "user_agent")
  String userAgent;
  @Column(name = "correlation_id", nullable = false, unique = true)
  String correlationId;

  public ClickEvent(String shortCode, String originalUrl, LocalDateTime clickedAt, String userAgent, String correlationId) {
    this.shortCode = shortCode;
    this.originalUrl = originalUrl;
    this.clickedAt = clickedAt;
    this.userAgent = userAgent;
    this.correlationId = correlationId;
  }

  protected ClickEvent() {
  }
}
