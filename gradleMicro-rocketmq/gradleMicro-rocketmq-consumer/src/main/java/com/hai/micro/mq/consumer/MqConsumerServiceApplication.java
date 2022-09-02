package com.hai.micro.mq.consumer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;

/**
 * @ClassName ConsumerServiceApplication
 * @Description Mq消费服务启动类
 * @Author ZXH
 * @Date 2022/8/18 10:32
 * @Version 1.0
 **/
@SpringBootApplication(scanBasePackages = {"com.hai.micro.common", "com.hai.micro.mq.consumer"},
        exclude = {DruidDataSourceAutoConfigure.class})
@EnableDiscoveryClient
@EnableMethodCache(basePackages = "com.hai.micro.mq.consumer")
@EnableCreateCacheAnnotation
@MapperScan(basePackages = "com.hai.micro.mq.consumer.**.mapper")
public class MqConsumerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqConsumerServiceApplication.class, args);
    }
}
