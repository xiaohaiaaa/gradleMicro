package com.hai.micro.service.second;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;

/**
 * @author zxh
 */
@SpringBootApplication(scanBasePackages = {"com.hai.micro.common", "com.hai.micro.service.second"},
    exclude = {DruidDataSourceAutoConfigure.class})
@EnableAsync
@EnableScheduling
@MapperScan(basePackages = "com.hai.micro.service.second.**.mapper")
@EnableCaching
@EnableMethodCache(basePackages = "com.hai.micro.service.second")
@EnableCreateCacheAnnotation
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.hai.micro.common.token.feign")
public class SecondApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecondApplication.class, args);
    }

}
