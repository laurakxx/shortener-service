package com.laura.shortener_service.dto;

import java.time.LocalDateTime;

public record LinkResponse(
    Long id,
    String shortCode,
    String originalUrl,
    int clicks,
    LocalDateTime createdAt,
    LocalDateTime expiresAt
) {
}
