package com.laura.analytics_service.service;

import com.laura.analytics_service.entity.FailedEvent;
import com.laura.analytics_service.repository.FailedEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FailedEventService {

  private final FailedEventRepository failedEventRepository;

  public void save(
      String topic,
      int partition,
      long offset,
      byte[] payload,
      String errorMessage
  ) {
    FailedEvent failedEvent = new FailedEvent(
        topic,
        partition,
        offset,
        payload,
        errorMessage
    );

    failedEventRepository.save(failedEvent);
  }
  public List<FailedEvent> getFailedEventsList() {
    return failedEventRepository.findAll();
  }
}