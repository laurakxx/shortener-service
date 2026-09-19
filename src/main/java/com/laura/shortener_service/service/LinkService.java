package com.laura.shortener_service.service;

import com.laura.shortener_service.dto.ClickStatsResponse;
import com.laura.shortener_service.dto.CreateLinkRequest;
import com.laura.shortener_service.dto.LinkResponse;
import com.laura.shortener_service.entity.Link;
import com.laura.shortener_service.entity.OutboxEvent;
import com.laura.shortener_service.entity.OutboxStatus;
import com.laura.shortener_service.event.LinkClickedEvent;
import com.laura.shortener_service.exception.LinkExpiredException;
import com.laura.shortener_service.exception.LinkNotFoundException;
import com.laura.shortener_service.mapper.LinkMapper;
import com.laura.shortener_service.repository.LinkRepository;
import com.laura.shortener_service.repository.OutboxEventRepository;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
@Slf4j
@Service
@RequiredArgsConstructor
public class LinkService {
  private final LinkRepository linkRepository;
  private final LinkMapper linkMapper;
  private final MeterRegistry meterRegistry;
  private final OutboxEventRepository outboxEventRepository;
  private final ObjectMapper objectMapper;

  @Transactional
  public LinkResponse createLink(CreateLinkRequest request) {
    Link savedLink = linkRepository.save(linkMapper.toEntity(request));
    meterRegistry.counter("links_created").increment();
    return linkMapper.toResponse(savedLink);
  }
  @Transactional
  public String redirect(String shortCode, String userAgent) {
    Link link =  findLinkByShortCode(shortCode);
    if (link.getExpiresAt() != null && link.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new LinkExpiredException(shortCode);
    }
    meterRegistry.counter("links.clicks").increment();

    link.setClicks(link.getClicks() + 1); //кол-во кликов
    linkRepository.save(link);

    String correlationId = MDC.get("correlationId");

    LinkClickedEvent event = new LinkClickedEvent(
        link.getShortCode(),
        link.getOriginalUrl(),
        LocalDateTime.now(),
        userAgent,
        correlationId
    );

    OutboxEvent outboxEvent = new OutboxEvent(
        "LINK",
        link.getShortCode(),
        "LINK_CLICKED",
        objectMapper.writeValueAsString(event),
        OutboxStatus.PENDING
        );
    outboxEventRepository.save(outboxEvent);

    log.info("Redirect for {}", shortCode);
    return link.getOriginalUrl();
  }

  @Cacheable(value = "links", key = "#shortCode")
  @Transactional(readOnly = true)
  public LinkResponse findInformation(String shortCode) {
    Link link = findLinkByShortCode(shortCode);
    return linkMapper.toResponse(link);
  }

  private Link findLinkByShortCode(String shortCode) {
    Link link = linkRepository.findByShortCode(shortCode).orElseThrow(()->new LinkNotFoundException());
    return link;
  }
  @CacheEvict(value = "links", key = "#shortCode")
  @Transactional
  public void delete(String shortCode) {
    Link link = findLinkByShortCode(shortCode);
    linkRepository.delete(link);
  }
  @Transactional(readOnly = true)
  public ClickStatsResponse getShortCodeStats(String shortCode) {
    Link link = findLinkByShortCode(shortCode);
    return new ClickStatsResponse(link.getClicks());
  }
}
