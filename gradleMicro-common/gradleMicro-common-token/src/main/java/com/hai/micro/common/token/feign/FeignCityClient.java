package com.hai.micro.common.token.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hai.micro.common.other.entity.City;
import com.hai.micro.common.token.handler.FeignAuthInterceptor;

/**
 * @ClassName FeignClient
 * @Description feign客户端
 * @Author ZXH
 * @Date 2022/7/15 11:08
 * @Version 1.0
 **/
@FeignClient(name = "gradleMicro-service-test", path = "/test/feign", configuration = FeignAuthInterceptor.class)
public interface FeignCityClient {

    /**
     * feign调用请求测试
     *
     * @param id
     * @return
     */
    @GetMapping("/add/ehcache")
    City myTestForEhcache(@RequestParam("id") Long id);
}
