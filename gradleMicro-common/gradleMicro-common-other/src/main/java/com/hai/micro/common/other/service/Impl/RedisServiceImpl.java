package com.hai.micro.common.other.service.Impl;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.hai.micro.common.other.service.RedisService;

/**
 * @author zxh
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Resource(name = "myRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void put(String key, Object value, long millis) {
        redisTemplate.opsForValue().set(key, value, millis, TimeUnit.MINUTES);
    }

    @Override
    public void putForHash(String objectkey, String hashKey, String value) {
        redisTemplate.opsForHash().put(objectkey, hashKey, value);
    }

    @Override
    public <T> T get(String key, Class<T> type) {
        return (T) redisTemplate.boundValueOps(key).get();
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean expire(String key, long millis) {
        return redisTemplate.expire(key, millis, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean persist(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public String getString(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public Long decrByKey(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }
}
