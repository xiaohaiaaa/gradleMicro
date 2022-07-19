package com.hai.micro.service.second.firstTest.service.Impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.anno.SerialPolicy;
import com.hai.micro.common.other.entity.City;
import com.hai.micro.common.other.service.RedisService;
import com.hai.micro.common.token.feign.FeignCityClient;
import com.hai.micro.service.second.constant.ServiceCachePrefixConstant;
import com.hai.micro.service.second.firstTest.service.TestService;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName TestServiceImpl
 * @Description 测试类
 * @Author ZXH
 * @Date 2022/7/15 10:57
 * @Version 1.0
 **/
@Service
@Slf4j
public class TestServiceImpl implements TestService {

    @Autowired
    private FeignCityClient feignCityClient;
    @Autowired
    private RedisService redisService;

    @Override
    @Cached(name = ServiceCachePrefixConstant.CITY_DETAIL_INFO, cacheType = CacheType.REMOTE, key = "#id", expire = 1,
        timeUnit = TimeUnit.MINUTES)
    public City getFeignInfo(Long id) {
        return feignCityClient.myTestForEhcache(id);
    }

    @Override
    public void insertRedisInfo(Long id) {
        City result = feignCityClient.myTestForEhcache(id);
        redisService.put(ServiceCachePrefixConstant.CITY_DETAIL_INFO + id, result, 1);
    }
}
