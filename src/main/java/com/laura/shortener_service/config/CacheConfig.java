package com.laura.shortener_service.config;

import org.jspecify.annotations.Nullable;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig implements CachingConfigurer {
  @Override
  public CacheErrorHandler errorHandler() {

    return new SimpleCacheErrorHandler() {
      @Override
      public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {

      }

      @Override
      public void handleCachePutError(RuntimeException exception, Cache cache, Object key, @Nullable Object value) {

      }

      @Override
      public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {

      }

      @Override
      public void handleCacheClearError(RuntimeException exception, Cache cache) {
      }
    };
  }
}
