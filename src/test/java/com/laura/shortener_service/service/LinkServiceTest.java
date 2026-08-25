package com.laura.shortener_service.service;

import com.laura.shortener_service.dto.CreateLinkRequest;
import com.laura.shortener_service.dto.LinkResponse;
import com.laura.shortener_service.exception.LinkNotFoundException;
import com.laura.shortener_service.repository.LinkRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LinkServiceTest {
  private static final String ORIGINAL_URL = "https://example.com/products/spring-boot-course";
  private static final String SHORT_CODE = "a7f3c9d2";
  @Autowired
  private LinkRepository linkRepository;

  @Autowired
  private LinkService linkService;

  @AfterEach
  void clearLinkRepository() {
    linkRepository.deleteAll();
  }
  @Test
  void shouldCreateLink() {
    CreateLinkRequest request = new CreateLinkRequest(ORIGINAL_URL);
    LinkResponse response = linkService.createLink(request);

    assertEquals(ORIGINAL_URL, response.originalUrl());
    assertNotNull(response.id());
    assertEquals(8, response.shortCode().length());
  }
  @Test
  void shouldReturnOriginalLink_whenSearchByShortCode() {
    CreateLinkRequest request = new CreateLinkRequest(ORIGINAL_URL);
    LinkResponse firstResponse = linkService.createLink(request);
    String shortCode = firstResponse.shortCode();

    String originalUrl = linkService.redirect(shortCode);

    assertEquals(ORIGINAL_URL, originalUrl);
  }
  @Test
  void shouldThrow_LinkNotFoundException() {
    assertThrows(LinkNotFoundException.class, ()-> linkService.findInformation(SHORT_CODE));
  }

}
