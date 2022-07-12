package com.hai.micro.gateway.gray;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.ExtendBalancer;
import com.alibaba.cloud.nacos.ribbon.NacosRule;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.hai.micro.gateway.constant.CloudGrayConstants;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.Server;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName NacosVersionRule
 * @Description 负载均衡算法实现
 * @Author ZXH
 * @Date 2022/6/9 10:07
 * @Version 1.0
 **/
@Slf4j
public class NacosVersionRule extends NacosRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public Server choose(Object key) {
        try {
            // 判断key是否灰度路由header
            String isGrayHeader = CloudGrayConstants.COMMON_JUDGE_NO;
            if (key instanceof JSONObject) {
                JSONObject grayHeader = (JSONObject)key;
                if (grayHeader.getString(CloudGrayConstants.GATEWAY_GRAY) != null) {
                    isGrayHeader = grayHeader.getString(CloudGrayConstants.GATEWAY_GRAY);
                }
            }
            // 获取配置文件中所配置的元数据信息
            String clusterName = this.nacosDiscoveryProperties.getClusterName();
            String isGrey = this.nacosDiscoveryProperties.getMetadata().get("isGray");
            String targetVersion = this.nacosDiscoveryProperties.getMetadata().get("targetVersion");

            // 获取负载均衡对象
            DynamicServerListLoadBalancer loadBalancer = (DynamicServerListLoadBalancer)getLoadBalancer();
            String name = loadBalancer.getName();
            log.debug("name:{},isGrey:{},isGrayHeader:{},targetVersion:{}", name, isGrey, isGrayHeader, targetVersion);

            // 获取所有可用实例
            NamingService namingService = this.nacosDiscoveryProperties.namingServiceInstance();
            List<Instance> instances = namingService.selectInstances(name, true);
            if (CollectionUtils.isEmpty(instances)) {
                log.warn("no instance in service {}", name);
                return null;
            }
            List<Instance> metadataMatchInstances = instances;

            // 是否进行灰度路由选择 1：服务灰度，2：请求头灰度
            log.debug("instances:{}", instances.toString());
            if (CloudGrayConstants.COMMON_JUDGE_YES.equals(isGrey)
                || CloudGrayConstants.COMMON_JUDGE_YES.equals(isGrayHeader)) {
                List<Instance> grayInstances =
                    instances.stream().filter(instance -> Objects.equals(CloudGrayConstants.COMMON_JUDGE_YES,
                        instance.getMetadata().get("isGray"))).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(grayInstances)) {
                    metadataMatchInstances = grayInstances;
                } else {
                    log.warn("not found gray instances name = {}, clusterName = {}, instance = {} ", name, clusterName,
                        instances);
                }
            } else {
                if (StringUtils.isNotBlank(targetVersion)) {
                    List<Instance> sameTargetInstances = instances.stream()
                        .filter(instance -> Objects.equals(targetVersion, instance.getMetadata().get("version")))
                        .collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(sameTargetInstances)) {
                        metadataMatchInstances = sameTargetInstances;
                    } else {
                        log.error(
                            "not found target version instances name = {}, clusterName = {}, instance = {} ,targetVersion = {}",
                            name, clusterName, instances, targetVersion);
                    }
                }
            }
            log.debug("metadataMatchInstances:{}", metadataMatchInstances);

            List<Instance> instancesToChoose = metadataMatchInstances;
            // 获取同一集群实例
            if (StringUtils.isNotBlank(clusterName)) {
                List<Instance> sameClusterInstances = metadataMatchInstances.stream()
                    .filter(instance -> Objects.equals(clusterName, instance.getClusterName()))
                    .collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(sameClusterInstances)) {
                    instancesToChoose = sameClusterInstances;
                } else {
                    log.warn("A cross-cluster call occurs name = {}, clusterName = {}, instance = {}", name,
                        clusterName, instances);
                }
            }
            log.debug("instancesToChoose:{}", instancesToChoose);

            Instance instance = ExtendBalancer.getHostByRandomWeight2(instancesToChoose);
            log.debug("instance:{}", instance.toString());

            return new NacosServer(instance);
        } catch (Exception e) {
            log.warn("NacosVersionRule error", e);
            return null;
        }

    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        super.initWithNiwsConfig(clientConfig);
        log.info("initWithNiwsConfig:{}", clientConfig);
    }
}
