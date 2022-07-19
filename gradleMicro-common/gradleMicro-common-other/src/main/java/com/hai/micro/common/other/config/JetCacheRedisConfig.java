package com.hai.micro.common.other.config;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import com.alicp.jetcache.CacheValueHolder;
import com.alicp.jetcache.anno.support.SpringConfigProvider;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
        // 解决localDateTime无法被反序列化问题
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }
}
