package com.hai.micro.service.second.firstTest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hai.micro.common.other.anno.ManageApiAuth;
import com.hai.micro.common.other.api.ResponseModel;
import com.hai.micro.common.other.entity.City;
import com.hai.micro.service.second.firstTest.service.TestService;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName TestController
 * @Description 测试类
 * @Author ZXH
 * @Date 2022/7/15 10:44
 * @Version 1.0
 **/
@RestController
@Slf4j
@RequestMapping("/second/test")
public class TestController {

    @Autowired
    private TestService testService;

    /**
     * 测试feign调用获取信息
     *
     * @return
     */
    @GetMapping("feign/detail")
    @ManageApiAuth
    public ResponseModel<City> getFeignInfo(@RequestParam("id")Long id) {
        return ResponseModel.success(testService.getFeignInfo(id));
    }

    /**
     * 测试redis缓存写入
     *
     * @param id
     * @return
     */
    @GetMapping("redis/insert")
    @ManageApiAuth
    public ResponseModel<String> insertRedisInfo(@RequestParam("id")Long id) {
        testService.insertRedisInfo(id);
        return ResponseModel.success();
    }
}
