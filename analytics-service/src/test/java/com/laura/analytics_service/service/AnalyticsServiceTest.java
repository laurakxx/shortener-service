package com.laura.analytics_service.service;

import com.laura.analytics_service.entity.ClickEvent;
import com.laura.analytics_service.event.LinkClickedEvent;
import com.laura.analytics_service.repository.ClickEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {
  private static final String SHORT_CODE = "a7f3c9d2";
  private static final String ORIGINAL_URL = "https://example.com";
  private static final String USER_AGENT = "test-user-agent";

  @Mock
  private ClickEventRepository clickEventRepository;

  @InjectMocks
  private AnalyticsService analyticsService;

  @Test
  void recordClick_savesEventToRepository() {
    LinkClickedEvent event = new LinkClickedEvent(
        SHORT_CODE,
        ORIGINAL_URL,
        LocalDateTime.now(),
        USER_AGENT,
        "correlation-1"
    );

    when(clickEventRepository.existsByCorrelationId("correlation-1")).thenReturn(false);

    analyticsService.recordClick(event);

    verify(clickEventRepository).save(any(ClickEvent.class));
  }

  @Test
  void recordClick_sameShortCodeWithDifferentCorrelationIds_savesBothEvents() {
    LinkClickedEvent firstEvent = new LinkClickedEvent(
        SHORT_CODE,
        ORIGINAL_URL,
        LocalDateTime.now(),
        USER_AGENT,
        "correlation-1"
    );

    LinkClickedEvent secondEvent = new LinkClickedEvent(
        SHORT_CODE,
        ORIGINAL_URL,
        LocalDateTime.now(),
        USER_AGENT,
        "correlation-2"
    );

    when(clickEventRepository.existsByCorrelationId("correlation-1")).thenReturn(false);
    when(clickEventRepository.existsByCorrelationId("correlation-2")).thenReturn(false);

    analyticsService.recordClick(firstEvent);
    analyticsService.recordClick(secondEvent);

    verify(clickEventRepository, times(2)).save(any(ClickEvent.class));
  }
}