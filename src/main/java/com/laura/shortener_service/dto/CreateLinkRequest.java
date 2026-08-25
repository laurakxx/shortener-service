package com.laura.shortener_service.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

public class CreateLinkRequest {
  @NotBlank(message="URL must be not blank")
  @URL(message = "Not valid URL format")
  private String originalUrl;

  @Future(message = "The expiration date must be in future")
  private LocalDateTime expiresAt;

  public CreateLinkRequest(String originalUrl) {
    this.originalUrl = originalUrl;
  }

  public CreateLinkRequest(String originalUrl, LocalDateTime expiresAt) {
    this.originalUrl = originalUrl;
    this.expiresAt = expiresAt;
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  public LocalDateTime getExpiresAt() {
    return expiresAt;
  }
}
