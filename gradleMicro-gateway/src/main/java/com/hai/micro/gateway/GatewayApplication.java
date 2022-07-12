package com.hai.micro.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName GatewayApplication
 * @Description
 * @Author ZXH
 * @Date 2022/7/5 19:05
 * @Version 1.0
 **/
@SpringBootApplication
@EnableDiscoveryClient
//@LoadBalancerClient(name = "gradleMicro-service-test", configuration = GrayBalancerConfig.class)
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
