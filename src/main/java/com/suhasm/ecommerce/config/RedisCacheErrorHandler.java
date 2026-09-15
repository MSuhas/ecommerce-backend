package com.suhasm.ecommerce.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisCacheErrorHandler implements CacheErrorHandler {

    @PostConstruct
    public void init() {
        log.info(">>> RedisCacheErrorHandler initialized <<<");
    }

    @Override
    public void handleCacheGetError(
            RuntimeException exception,
            Cache cache,
            Object key) {

        log.warn(
                "Redis GET failed. cache={}, key={}. Continuing without cache.",
                cache.getName(),
                key,
                exception
        );
    }

    @Override
    public void handleCachePutError(
            RuntimeException exception,
            Cache cache,
            Object key,
            Object value) {

        log.warn(
                "Redis PUT failed. cache={}, key={}. Continuing without cache.",
                cache.getName(),
                key,
                exception
        );
    }

    @Override
    public void handleCacheEvictError(
            RuntimeException exception,
            Cache cache,
            Object key) {

        log.warn(
                "Redis EVICT failed. cache={}, key={}.",
                cache.getName(),
                key,
                exception
        );
    }

    @Override
    public void handleCacheClearError(
            RuntimeException exception,
            Cache cache) {

        log.warn(
                "Redis CLEAR failed. cache={}.",
                cache.getName(),
                exception
        );
    }
}