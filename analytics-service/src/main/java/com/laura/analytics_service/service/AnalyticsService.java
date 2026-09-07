package com.laura.analytics_service.service;

import com.laura.analytics_service.dto.AnalyticsResponse;
import com.laura.analytics_service.entity.ClickEvent;
import com.laura.analytics_service.event.LinkClickedEvent;
import com.laura.analytics_service.repository.ClickEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
  private final ClickEventRepository clickEventRepository;

  @Transactional
  public void recordClick(LinkClickedEvent event) {

    if(clickEventRepository.existsByCorrelationId(event.correlationId())) {
      return;
    }
    ClickEvent clickEvent = new ClickEvent(
        event.shortCode(),
        event.originalUrl(),
        event.clickedAt(),
        event.userAgent(),
        event.correlationId()
    );
    clickEventRepository.save(clickEvent);
  }

  @Transactional(readOnly = true)
  public AnalyticsResponse getAnalyticsByShortCode(String shortCode) {
    long totalClicks = clickEventRepository.countByShortCode(shortCode);
    return new AnalyticsResponse(shortCode, totalClicks);
  }
}
