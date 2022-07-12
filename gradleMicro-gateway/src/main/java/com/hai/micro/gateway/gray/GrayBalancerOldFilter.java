package com.hai.micro.gateway.gray;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * @ClassName GrayBalancerOldFilter
 * @Description 基于ribbon配置自定义负载均衡策略-LoadBalancerClientFilter已过期
 * @Author ZXH
 * @Date 2022/7/11 14:36
 * @Version 1.0
 **/
@Configuration(proxyBeanMethods = false)
@Slf4j
public class GrayBalancerOldFilter {
/*public class GrayBalancerFilter extends LoadBalancerClientFilter {

    @Autowired
    private GrayRuleHandler ruleHandler;

    public GrayBalancerFilter(LoadBalancerClient loadBalancer, LoadBalancerProperties properties) {
        super(loadBalancer, properties);
    }

    @Override
    protected ServiceInstance choose(ServerWebExchange exchange) {
        if (loadBalancer instanceof RibbonLoadBalancerClient) {
            RibbonLoadBalancerClient ribbonLoadBalancer = (RibbonLoadBalancerClient)this.loadBalancer;
            String serviceId = ((URI)exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR)).getHost();

            return ribbonLoadBalancer.choose(serviceId, ruleHandler.handle(exchange));
        } else {
            return super.choose(exchange);
        }
    }*/

}
