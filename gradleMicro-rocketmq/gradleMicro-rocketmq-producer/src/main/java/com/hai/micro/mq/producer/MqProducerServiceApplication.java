package com.hai.micro.mq.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName ConsumerServiceApplication
 * @Description Mq生产者服务启动类
 * @Author ZXH
 * @Date 2022/8/18 10:32
 * @Version 1.0
 **/
@SpringBootApplication
@EnableFeignClients(basePackages = "com.hai.micro.common.token.feign")
@EnableDiscoveryClient
public class MqProducerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqProducerServiceApplication.class, args);
    }
}
