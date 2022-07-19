package com.hai.micro.common.other.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author ZXH
 * redis序列化配置
 */
@Configuration(proxyBeanMethods = false)
public class RedisConfiguration {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 解决redisTemplate存储乱码问题
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean("myRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        redisConnectionFactory.setValidateConnection(true);
        redisConnectionFactory.setShareNativeConnection(true);
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //创建一个json的序列化对象
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        //设置value的序列化方式json
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        stringRedisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        stringRedisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        //redisTemplate.setEnableTransactionSupport(true);是否支持事务
        redisTemplate.afterPropertiesSet();
        stringRedisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
