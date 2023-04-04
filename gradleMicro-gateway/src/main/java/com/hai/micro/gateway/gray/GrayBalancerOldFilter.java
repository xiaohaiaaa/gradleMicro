package com.hai.micro.gateway.gray;

import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName GrayBalancerOldFilter
 * @Description 基于ribbon配置自定义负载均衡策略-LoadBalancerClientFilter已过期，搭配NacosVersionRule使用
 *
 * 服务发布，流量切换实现：
 * 1、项目引入spring-cloud-starter-alibaba-nacos-discovery
 * 2、NacosServerList的getUpdatedListOfServers方法，会不断刷新在线服务列表，并更新到服务器本地缓存中
 * 3、当服务配置权重设置为0时，服务相当于下线，getUpdatedListOfServers方法获取更新时会删除该服务缓存
 * 4、项目引入spring-cloud-gateway-server
 * 5、ReactiveLoadBalancerClientFilter的choose方法会对请求进行负载均衡处理，可重写
 * 6、请求到RibbonLoadBalancerClient的choose方法，再到BaseLoadBalancer的choose方法
 * 7、BaseLoadBalancer#choose这里会去获取上面存储的服务列表信息，根据负载均衡算法进行计算(默认算法是NacosRule，可重写为NacosVersionRule并在env配置)，返回最终处理请求的服务信息
 *
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
