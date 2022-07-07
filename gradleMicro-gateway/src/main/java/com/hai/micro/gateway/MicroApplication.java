package com.hai.micro.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName MicroApplication
 * @Description
 * @Author ZXH
 * @Date 2022/7/5 19:05
 * @Version 1.0
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class MicroApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroApplication.class, args);
    }
}
