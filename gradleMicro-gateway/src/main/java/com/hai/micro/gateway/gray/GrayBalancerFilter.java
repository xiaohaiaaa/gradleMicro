package com.hai.micro.gateway.gray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSONObject;
import com.hai.micro.common.other.constant.BizConstants;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @ClassName GrayBalanceFilter
 * @Description 灰度负载均衡客户端
 *  1：基于ribbon路由选择服务实例进行灰度路由实现
 *  2：基于规则把需要路由的信息放到请求头
 *  3：底层基于覆写Nacos的路由选择规格进行服务实例选择
 * @Author ZXH
 * @Date 2022/6/2 17:25
 * @Version 1.0
 **/
@Slf4j
//@Configuration(proxyBeanMethods = false)
public class GrayBalancerFilter extends ReactiveLoadBalancerClientFilter {

    @Autowired
    private GrayRuleHandler grayRuleHandler;

    public GrayBalancerFilter(LoadBalancerClientFactory clientFactory, GatewayLoadBalancerProperties properties) {
        super(clientFactory, properties);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        JSONObject jsonObject = grayRuleHandler.handle(exchange);
        exchange.getAttributes().put(BizConstants.GATEWAY_GRAY, jsonObject.get(BizConstants.GATEWAY_GRAY));
        return super.filter(exchange, chain);
    }
}
