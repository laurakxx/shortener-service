package com.laura.analytics_service.entity;

import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "failed_events")
public class FailedEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String topic;

  @Column(name = "partition_number", nullable = false)
  private int partitionNumber;

  @Column(name = "message_offset", nullable = false)
  private long messageOffset;

  @Column(nullable = false)
  private byte[] payload;

  @Column(name = "error_message")
  private String errorMessage;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  protected FailedEvent() {
  }

  public FailedEvent(
      String topic,
      int partitionNumber,
      long messageOffset,
      byte[] payload,
      String errorMessage
  ) {
    this.topic = topic;
    this.partitionNumber = partitionNumber;
    this.messageOffset = messageOffset;
    this.payload = payload;
    this.errorMessage = errorMessage;
    this.createdAt = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public String getTopic() {
    return topic;
  }

  public int getPartitionNumber() {
    return partitionNumber;
  }

  public long getMessageOffset() {
    return messageOffset;
  }

  public byte[] getPayload() {
    return payload;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}