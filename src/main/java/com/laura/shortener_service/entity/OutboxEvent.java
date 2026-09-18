package com.laura.shortener_service.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
public class OutboxEvent {
  @Id

  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, name = "aggregate_type")
  private String aggregateType;

  @Column(nullable = false, name = "aggregate_id")
  private String aggregateId;

  @Column(nullable = false, name = "event_type")
  private String eventType;

  @Column(nullable = false)
  private String payload;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private OutboxStatus status;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
  @PrePersist
  public void prePersist() {
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
  }

  @Column(name = "sent_at", nullable = true)
  private LocalDateTime sentAt;

  protected OutboxEvent() {
  }

  public OutboxEvent(String aggregateType, String aggregateId, String eventType, String payload, OutboxStatus status) {
    this.aggregateType = aggregateType;
    this.aggregateId = aggregateId;
    this.eventType = eventType;
    this.payload = payload;
    this.status = status;
  }

  public void setStatus(OutboxStatus status) {
    this.status = status;
  }

  public String getPayload() {
    return payload;
  }

  public void setSentAt(LocalDateTime sentAt) {
    this.sentAt = sentAt;
  }

  public UUID getId() {
    return id;
  }

  public OutboxStatus getStatus() {
    return status;
  }
}
