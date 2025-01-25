package com.example.main.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Supplier;

@Service
public class CacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    // Delete data from the cache
    public void deleteFromCache(String key, String hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    public <T> T getOrCache(String cacheName, String key, Supplier<T> retrievalFunction) {
        // Attempt to get data from cache
        T cachedData = (T) getFromHashCache(cacheName, key);

        if (cachedData == null) {
            cachedData = retrievalFunction.get();
            if (cachedData != null) {
                putInHashCache(cacheName, key, cachedData);
            }
        }

        return cachedData;
    }

    public void setCacheExpiration(String cacheName, long timeoutInSeconds) {
        redisTemplate.expire(cacheName, Duration.ofSeconds(timeoutInSeconds));
    }
    // Retrieve data from a Redis hash using a hash key and field
    public Object getFromHashCache(String cacheName, String field) {
        return redisTemplate.opsForHash().get(cacheName, field);
    }

    // Save or update data in a Redis hash using a hash key and field
    public void putInHashCache(String cacheName, String field, Object value) {
        redisTemplate.opsForHash().put(cacheName, field, value);
    }
}
