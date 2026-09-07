package com.laura.analytics_service.service;

import com.laura.analytics_service.event.LinkClickedEvent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FailedEventService {
  private final List<LinkClickedEvent> failedEventsList = new ArrayList<>();

  public void add(LinkClickedEvent event){
    failedEventsList.add(event);
  }

  public List<LinkClickedEvent> getFailedEventsList() {
    return List.copyOf(failedEventsList);
  }
}
