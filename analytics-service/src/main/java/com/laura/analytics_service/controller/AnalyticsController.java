package com.laura.analytics_service.controller;

import com.laura.analytics_service.dto.AnalyticsResponse;
import com.laura.analytics_service.event.LinkClickedEvent;
import com.laura.analytics_service.service.AnalyticsService;
import com.laura.analytics_service.service.FailedEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
  private final AnalyticsService analyticsService;
  private final FailedEventService failedEventService;

  @GetMapping("/{shortCode}")
  public ResponseEntity<AnalyticsResponse> getAnalytics(@PathVariable String shortCode){
    AnalyticsResponse response = analyticsService.getAnalyticsByShortCode(shortCode);
    return ResponseEntity.ok(response);
  }
  @GetMapping("/failed")
  public ResponseEntity<List<LinkClickedEvent>> getFailedEvents(){
    return ResponseEntity.ok(failedEventService.getFailedEventsList());
  }
}
