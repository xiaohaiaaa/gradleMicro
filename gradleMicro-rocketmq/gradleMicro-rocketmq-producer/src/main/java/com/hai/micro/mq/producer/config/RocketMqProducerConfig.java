package com.hai.micro.mq.producer.config;

import javax.annotation.PostConstruct;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.stereotype.Component;

/**
 * @ClassName RocketMqProducerConfig
 * @Description mq生产者配置类
 * @Author ZXH
 * @Date 2022/7/21 18:19
 * @Version 1.0
 **/
@Component
public class RocketMqProducerConfig {

    private static DefaultMQProducer producer;

    /**
     * 初始化mq生产者配置
     *
     * @throws MQClientException
     */
    @PostConstruct
    public void initProducer() throws MQClientException {
        // 实例化消息生产者Producer
        producer = new DefaultMQProducer("default_group");
        // 设置NameServer的地址
        producer.setNamesrvAddr("localhost:9876");
        // 设置重试次数
        producer.setRetryTimesWhenSendAsyncFailed(0);
        // 启动Producer实例
        producer.start();
    }

    /**
     * 获取生产者
     *
     * @return
     */
    public DefaultMQProducer getProducer() {
        return producer;
    }
}
