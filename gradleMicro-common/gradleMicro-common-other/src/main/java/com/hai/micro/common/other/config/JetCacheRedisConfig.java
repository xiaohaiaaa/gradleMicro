package com.hai.micro.common.other.config;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import com.alicp.jetcache.CacheValueHolder;
import com.alicp.jetcache.anno.support.SpringConfigProvider;

/**
 * @ClassName JetCacheRedisConfig
 * @Description jetCache存储序列化配置
 * @Author ZXH
 * @Date 2022/7/19 15:45
 * @Version 1.0
 **/
@Configuration(proxyBeanMethods = false)
public class JetCacheRedisConfig extends SpringConfigProvider {

    @Autowired
    private GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer;

    @Override
    public Function<Object, byte[]> parseValueEncoder(String valueEncoder) {
        return (v) -> this.genericJackson2JsonRedisSerializer.serialize(v);
    }

    @Override
    public Function<byte[], Object> parseValueDecoder(String valueDecoder) {
        return (v) -> this.genericJackson2JsonRedisSerializer.deserialize(v, CacheValueHolder.class);
    }

    @Bean({"genericJackson2JsonRedisSerializer"})
    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
}
