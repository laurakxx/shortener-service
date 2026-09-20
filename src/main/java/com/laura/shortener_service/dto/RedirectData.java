package com.laura.shortener_service.dto;

import java.time.LocalDateTime;

public record RedirectData(
    String originalUrl,
    LocalDateTime expiresAt
    ) {
}
