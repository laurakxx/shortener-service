package com.laura.shortener_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "links")
public class Link {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "short_code", nullable = false, unique = true)
  private String shortCode;

  @Column(name = "original_url", nullable = false)
  private String originalUrl;

  private int clicks;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
  @PrePersist
  public void prePersist() {
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
  }

  @Column(name = "expires_at")
  private LocalDateTime expiresAt;

  protected Link() {
  }

  public Link(String shortCode, String originalUrl, int clicks, LocalDateTime createdAt, LocalDateTime expiresAt) {
    this.shortCode = shortCode;
    this.originalUrl = originalUrl;
    this.clicks = clicks;
    this.createdAt = createdAt;
    this.expiresAt = expiresAt;
  }
  public Link(String shortCode, String originalUrl, LocalDateTime expiresAt) {
    this.shortCode = shortCode;
    this.originalUrl = originalUrl;
    this.expiresAt = expiresAt;
  }

  public Long getId() {
    return id;
  }

  public String getShortCode() {
    return shortCode;
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  public int getClicks() {
    return clicks;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setClicks(int clicks) {
    this.clicks = clicks;
  }
}
