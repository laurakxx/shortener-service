package com.laura.shortener_service.mapper;

import com.laura.shortener_service.dto.CreateLinkRequest;
import com.laura.shortener_service.dto.LinkResponse;
import com.laura.shortener_service.entity.Link;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class LinkMapper {
  public LinkResponse toResponse(Link link){
    return new LinkResponse(
        link.getId(),
        link.getShortCode(),
        link.getOriginalUrl(),
        link.getClicks(),
        link.getCreatedAt(),
        link.getExpiresAt()
    );
  }
  public Link toEntity(CreateLinkRequest request) {
    String shortCode =  UUID.randomUUID().toString().substring(0, 8);
    return new Link(shortCode, request.originalUrl(), request.expiresAt());
  }
}
