package com.laura.analytics_service.dto;

public record AnalyticsResponse(
    String shortCode,
    long totalClicks
) {
}
