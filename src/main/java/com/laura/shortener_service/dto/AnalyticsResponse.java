package com.laura.shortener_service.dto;

public record AnalyticsResponse(
    String shortCode,
    long totalClicks
) {
}
