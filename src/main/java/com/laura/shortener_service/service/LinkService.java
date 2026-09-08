package com.laura.shortener_service.service;

import com.laura.shortener_service.dto.ClickStatsResponse;
import com.laura.shortener_service.dto.CreateLinkRequest;
import com.laura.shortener_service.dto.LinkResponse;
import com.laura.shortener_service.entity.Link;
import com.laura.shortener_service.event.LinkClickedEvent;
import com.laura.shortener_service.exception.LinkExpiredException;
import com.laura.shortener_service.exception.LinkNotFoundException;
import com.laura.shortener_service.mapper.LinkMapper;
import com.laura.shortener_service.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
@Slf4j
@Service
@RequiredArgsConstructor
public class LinkService {
  private final LinkRepository linkRepository;
  private final LinkMapper linkMapper;
  private final KafkaTemplate<String, LinkClickedEvent> kafkaTemplate;
  private static final String LINK_CLICKS_TOPIC = "link-clicks";

  @Transactional
  public LinkResponse createLink(CreateLinkRequest request) {
    Link savedLink = linkRepository.save(linkMapper.toEntity(request));
    return linkMapper.toResponse(savedLink);
  }
  @Transactional
  public String redirect(String shortCode, String userAgent) {
    Link link =  findLinkByShortCode(shortCode);
    if (link.getExpiresAt() != null && link.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new LinkExpiredException(shortCode);
    }
    link.setClicks(link.getClicks() + 1); //кол-во кликов
    String correlationId = MDC.get("correlationId");
    LinkClickedEvent event = new LinkClickedEvent(
        link.getShortCode(),
        link.getOriginalUrl(),
        LocalDateTime.now(),
        userAgent,
        correlationId
    );
    kafkaTemplate.send(LINK_CLICKS_TOPIC, event);
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
