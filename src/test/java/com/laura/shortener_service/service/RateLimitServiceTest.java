package com.laura.shortener_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class RateLimitServiceTest {

  private RedisTemplate<String, String> redisTemplate;
  private ValueOperations<String, String> valueOperations;
  private RateLimitService rateLimitService;

  @BeforeEach
  void setUp() {
    redisTemplate = mock(RedisTemplate.class);
    valueOperations = mock(ValueOperations.class);
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    rateLimitService = new RateLimitService(redisTemplate);
  }

  @Test
  void shouldNotBlockFirstRequest() {
    when(valueOperations.increment("rate:127.0.0.1")).thenReturn(1L);

    boolean result = rateLimitService.isRateLimited("127.0.0.1");

    assertFalse(result);
  }

  @Test
  void shouldBlockEleventhRequest() {
    when(valueOperations.increment("rate:127.0.0.1")).thenReturn(11L);

    boolean result = rateLimitService.isRateLimited("127.0.0.1");

    assertTrue(result);
  }

  @Test
  void shouldSetTtlForFirstRequest() {
    when(valueOperations.increment("rate:127.0.0.1")).thenReturn(1L);

    rateLimitService.isRateLimited("127.0.0.1");

    verify(redisTemplate).expire("rate:127.0.0.1", Duration.ofSeconds(60));
  }

  @Test
  void shouldNotResetTtlForSubsequentRequests() {
    when(valueOperations.increment("rate:127.0.0.1")).thenReturn(2L);

    rateLimitService.isRateLimited("127.0.0.1");

    verify(redisTemplate, never()).expire("rate:127.0.0.1", Duration.ofSeconds(60));
  }
}