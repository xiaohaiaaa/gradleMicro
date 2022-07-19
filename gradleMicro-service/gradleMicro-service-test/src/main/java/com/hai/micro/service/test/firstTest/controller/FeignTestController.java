package com.hai.micro.service.test.firstTest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hai.micro.common.other.entity.City;
import com.hai.micro.common.token.feign.FeignCityClient;
import com.hai.micro.service.test.firstTest.service.TestService;

/**
 * @ClassName FeignTestController
 * @Description feign请求控制类
 * @Author ZXH
 * @Date 2022/7/18 11:18
 * @Version 1.0
 **/
@RestController
@RequestMapping("/test/feign")
public class FeignTestController implements FeignCityClient {

    @Autowired
    private TestService testService;

    @Override
    public City myTestForEhcache(@RequestParam("id") Long id) {
        return testService.testEhcache(id);
    }
}
