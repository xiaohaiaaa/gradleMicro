package com.hai.micro.service.test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;

/**
 * @author zxh
 */
@SpringBootApplication(scanBasePackages = {"com.hai.micro.common", "com.hai.micro.service.test"},
    exclude = {DruidDataSourceAutoConfigure.class})
@EnableAsync
@EnableScheduling
@MapperScan(basePackages = "com.hai.micro.service.test.**.mapper")
@EnableCaching
@EnableDiscoveryClient
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

}
