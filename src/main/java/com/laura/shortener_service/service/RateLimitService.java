package com.laura.shortener_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimitService {
  private final RedisTemplate<String, String> redisTemplate;
  private static final int MAX_REQUESTS = 10;
  private static final Duration WINDOW = Duration.ofSeconds(60);

  public boolean isRateLimited(String ip) {
    try {
      String key = "rate:" + ip;
      Long requests = redisTemplate.opsForValue().increment(key);

      if(requests!=null && requests==1) {
        redisTemplate.expire(key, WINDOW);
      }
      return requests!=null && requests>MAX_REQUESTS;
    }
    catch (Exception e){
      log.warn("Rate limit check failed for ip={}", ip, e);
      return false;
    }

  }
}
