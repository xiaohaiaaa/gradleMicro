package com.hai.micro.mq.consumer.config;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import com.hai.micro.mq.consumer.bo.BaseSubscribeBO;
import lombok.extern.slf4j.Slf4j;
import com.hai.micro.mq.consumer.subscribe.service.SubscribeMessageService;

/**
 * @ClassName SubscribeInitConfig
 * @Description 订阅信息配置类
 * @Author ZXH
 * @Date 2022/8/12 10:21
 * @Version 1.0
 **/
@Slf4j
@Component
public class SubscribeMessageInitConfig {

    private static List<BaseSubscribeBO> serviceSubscribes;

    @Autowired
    private SubscribeMessageService subscribeMessageService;

    /**
     * 初始化服务订阅配置
     */
    @Autowired
    public void setServiceSubscribe() {
        serviceSubscribes = subscribeMessageService.findAllSubscribes();
        log.info("SubscribeInitConfig 初始化服务订阅配置成功 ServiceSubscribes Json:{}", JSON.toJSON(serviceSubscribes));
    }


    public List<BaseSubscribeBO> getServiceSubscribes() {
        return serviceSubscribes;
    }

    public void setServiceSubscribes(List<BaseSubscribeBO> subscribes) {
        serviceSubscribes = subscribes;
    }
}
