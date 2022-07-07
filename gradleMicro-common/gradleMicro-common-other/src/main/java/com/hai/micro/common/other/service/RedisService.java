package com.hai.micro.common.other.service;

public interface RedisService {

    void put(String key, Object value, long millis);

    void putForHash(String objectkey, String hashKey, String value);

    <T> T get(String key, Class<T> type);

    void remove(String key);

    boolean expire(String key, long millis);

    boolean persist(String key);

    String getString(String key);

    Long decrByKey(String key);
}
