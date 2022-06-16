package com.example.demo.factory;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;

import java.util.concurrent.TimeUnit;

public class CacheFactory {

    public Cache createExpireAfterWriteCacheByNameAndIntervalAndTimeUnit(
            String cacheName, int timeInterval, TimeUnit timeUnit) {
        return new CaffeineCache(
                cacheName,
                Caffeine
                        .newBuilder()
                        .expireAfterWrite(timeInterval, timeUnit)
                        .build()
        );
    }
}
