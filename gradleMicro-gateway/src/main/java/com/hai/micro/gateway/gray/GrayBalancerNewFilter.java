package com.hai.micro.gateway.gray;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerUriTools;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.gateway.support.DelegatingServiceInstance;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.fastjson.JSONObject;
import com.hai.micro.gateway.constant.CloudGrayConstants;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @ClassName GrayBalancerNewFilter
 * @Description 基于GlobalFilter的自定义负载均衡实现
 * @Author ZXH
 * @Date 2022/7/11 16:30
 * @Version 1.0
 **/
@Component
@Slf4j
public class GrayBalancerNewFilter implements GlobalFilter, Ordered {

    @Resource
    private final LoadBalancerClientFactory clientFactory;
    @Autowired
    private GrayRuleHandler grayRuleHandler;
    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    public GrayBalancerNewFilter(LoadBalancerClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public int getOrder() {
        return 10149;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        JSONObject handle = grayRuleHandler.handle(exchange);
        // 判断key是否灰度路由header
        String isGrayHeader = CloudGrayConstants.COMMON_JUDGE_NO;
        if (handle != null) {
            if (handle.getString(CloudGrayConstants.GATEWAY_GRAY) != null) {
                isGrayHeader = handle.getString(CloudGrayConstants.GATEWAY_GRAY);
            }
        }
        // 获取配置文件中所配置的元数据信息
        String clusterName = this.nacosDiscoveryProperties.getClusterName();
        String isGrey = this.nacosDiscoveryProperties.getMetadata().get("isGray");
        String targetVersion = this.nacosDiscoveryProperties.getMetadata().get("targetVersion");
        // 判断请求路径是否支持路由
        URI url = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        String schemePrefix = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR);
        if (url != null && ("lb".equals(url.getScheme()) || "lb".equals(schemePrefix))) {
            ServerWebExchangeUtils.addOriginalRequestUrl(exchange, url);
            log.info(ReactiveLoadBalancerClientFilter.class.getSimpleName() + " url before: " + url);

            String finalIsGrayHeader = isGrayHeader;
            return this.choose(exchange).doOnNext((response) -> {
                if (!response.hasServer()) {
                    throw NotFoundException.create(true, "Unable to find instance for " + url.getHost());
                } else {
                    URI uri = exchange.getRequest().getURI();
                    String overrideScheme = null;
                    if (schemePrefix != null) {
                        overrideScheme = url.getScheme();
                    }
                    // 获取所有可用实例
                    List<ServiceInstance> metadataMatchInstances = new ArrayList<>();
                    while (!metadataMatchInstances.contains(response.getServer())) {
                        try {
                            metadataMatchInstances.add(response.getServer());
                            response = this.choose(exchange).toFuture().get();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    // 判断是否灰度，取支持灰度的实例
                    if (CloudGrayConstants.COMMON_JUDGE_YES.equals(isGrey)
                            || CloudGrayConstants.COMMON_JUDGE_YES.equals(finalIsGrayHeader)) {
                        List<ServiceInstance> matchInstances = metadataMatchInstances.stream().filter(serviceInstance -> CloudGrayConstants.COMMON_JUDGE_YES.equals(serviceInstance.getMetadata().get("isGray"))).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(matchInstances)) {
                            metadataMatchInstances = matchInstances;
                        }
                    } else {
                        if (Strings.isNotBlank(targetVersion)) {
                            List<ServiceInstance> matchInstances = metadataMatchInstances.stream().filter(serviceInstance -> targetVersion.equals(serviceInstance.getMetadata().get("version"))).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(matchInstances)) {
                                metadataMatchInstances = matchInstances;
                            }
                        }
                    }
                    // 取相同集群下实例
                    if (Strings.isNotBlank(clusterName)) {
                        List<ServiceInstance> matchInstances = metadataMatchInstances.stream().filter(serviceInstance -> clusterName.equals(serviceInstance.getServiceId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(matchInstances)) {
                            metadataMatchInstances = matchInstances;
                        }
                    }
                    // 选取实例返回
                    DelegatingServiceInstance serviceInstance =
                        new DelegatingServiceInstance(metadataMatchInstances.get(RandomUtils.nextInt(metadataMatchInstances.size())), overrideScheme);
                    URI requestUrl = LoadBalancerUriTools.reconstructURI(serviceInstance, uri);
                    log.info("LoadBalancerClientFilter url chosen: " + requestUrl);
                    exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, requestUrl);
                }
            }).then(chain.filter(exchange));
        } else {
            return chain.filter(exchange);
        }
    }

    private Mono<Response<ServiceInstance>> choose(ServerWebExchange exchange) {
        URI uri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        assert uri != null;
        ReactorLoadBalancer<ServiceInstance> loadBalancer = this.clientFactory.getInstance(uri.getHost(),
            ReactorLoadBalancer.class, new Class[] {ServiceInstance.class});
        if (loadBalancer == null) {
            throw new NotFoundException("No loadbalancer available for " + uri.getHost());
        } else {
            return loadBalancer.choose(this.createRequest());
        }
    }

    private Request createRequest() {
        return ReactiveLoadBalancer.REQUEST;
    }
}

