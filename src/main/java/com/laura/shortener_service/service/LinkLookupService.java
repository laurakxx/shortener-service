package com.laura.shortener_service.service;

import com.laura.shortener_service.dto.RedirectData;
import com.laura.shortener_service.entity.Link;
import com.laura.shortener_service.exception.LinkNotFoundException;
import com.laura.shortener_service.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkLookupService {
  private final LinkRepository linkRepository;

  @Cacheable(value = "links", key = "#shortCode")
  @Transactional(readOnly = true)
  public RedirectData findForRedirect(String shortCode){
    Link link = linkRepository.findByShortCode(shortCode).orElseThrow(()->new LinkNotFoundException());
    return new RedirectData(link.getOriginalUrl(), link.getExpiresAt());
  }
}
