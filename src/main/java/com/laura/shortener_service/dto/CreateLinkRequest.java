package com.laura.shortener_service.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

public record CreateLinkRequest(
    @NotBlank(message = "URL must be not blank")
    @URL(message = "Not valid URL format")
    String originalUrl,

    @Future(message = "The expiration date must be in future")
    LocalDateTime expiresAt
) {
  public CreateLinkRequest(String originalUrl) {
    this(originalUrl, null);
  }
}