package com.laura.shortener_service.controller;

import com.laura.shortener_service.dto.ClickStatsResponse;
import com.laura.shortener_service.dto.CreateLinkRequest;
import com.laura.shortener_service.dto.LinkResponse;
import com.laura.shortener_service.service.LinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/links")
@RequiredArgsConstructor
public class LinkController {

  private final LinkService linkService;

  @PostMapping
  public ResponseEntity<LinkResponse> create(@Valid @RequestBody CreateLinkRequest request){
    LinkResponse response = linkService.createLink(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{shortCode}")
  public ResponseEntity<LinkResponse> getInformation(@PathVariable String shortCode) {
    LinkResponse response = linkService.findInformation(shortCode);
    return ResponseEntity.ok(response);
  }
  @DeleteMapping("/{shortCode}")
  public ResponseEntity<Void> delete(@PathVariable String shortCode) {
    linkService.delete(shortCode);
    return ResponseEntity.noContent().build();
  }
  @GetMapping("/{shortCode}/stats")
  public ResponseEntity<ClickStatsResponse> getShortCodeStats(@PathVariable String shortCode) {
    ClickStatsResponse response = linkService.getShortCodeStats(shortCode);
    return ResponseEntity.ok(response);
  }

}
