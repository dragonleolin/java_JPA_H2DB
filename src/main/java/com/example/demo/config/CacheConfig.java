package com.example.demo.config;

import com.example.demo.factory.CacheFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.demo.constant.CacheConst.CACHE_NAME_USER_CACHE;
import static com.example.demo.constant.CacheConst.CACHE_NAME_BOOK_CACHE;
import static com.example.demo.constant.PropertiesConst.CACHE_USER_CACHE_TIME_TO_LIVE;
import static com.example.demo.constant.PropertiesConst.CACHE_BOOK_CACHE_TIME_TO_LIVE;

@Configuration
@EnableCaching
public class CacheConfig {

    @Autowired
    private Environment env;

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(allCaches());
        return cacheManager;
    }

    @Bean
    public List<Cache> allCaches() {
        return Arrays.asList(
                userCache(),
                bookCache()
        );
    }

    @Bean
    public CacheFactory cacheFactory() {
        return new CacheFactory();
    }

    @Bean(name = CACHE_NAME_USER_CACHE)
    public Cache userCache() {
        return cacheFactory().createExpireAfterWriteCacheByNameAndIntervalAndTimeUnit(
                CACHE_NAME_USER_CACHE,
                Integer.parseInt(env.getRequiredProperty(CACHE_USER_CACHE_TIME_TO_LIVE)),
                TimeUnit.SECONDS
        );
    }
    

    @Bean(name = CACHE_NAME_BOOK_CACHE)
    public Cache bookCache() {
    	return cacheFactory().createExpireAfterWriteCacheByNameAndIntervalAndTimeUnit(
    			CACHE_NAME_BOOK_CACHE,
    			Integer.parseInt(env.getRequiredProperty(CACHE_BOOK_CACHE_TIME_TO_LIVE)),
    			TimeUnit.SECONDS
    			);
    }
}
