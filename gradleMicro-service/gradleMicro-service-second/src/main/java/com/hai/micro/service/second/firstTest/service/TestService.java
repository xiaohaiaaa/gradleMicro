package com.hai.micro.service.second.firstTest.service;

import com.hai.micro.common.other.entity.City;

/**
 * @ClassName TestService
 * @Description 测试类
 * @Author ZXH
 * @Date 2022/7/15 10:57
 * @Version 1.0
 **/
public interface TestService {

    /**
     * 测试feign调用获取信息
     *
     * @param id
     * @return
     */
    City getFeignInfo(Long id);

    /**
     * 测试redis缓存写入
     *
     * @param id
     */
    void insertRedisInfo(Long id);
}
