package com.laura.shortener_service.controller;

import com.laura.shortener_service.service.LinkService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class RedirectController {

  private final LinkService linkService;

  @GetMapping("/{shortCode}")
  public ResponseEntity<Void> redirect(@PathVariable String shortCode, HttpServletRequest request) {
    String userAgent = request.getHeader("User-Agent");
    String originalUrl = linkService.redirect(shortCode, userAgent);
    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();
  }
}
