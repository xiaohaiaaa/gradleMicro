package com.hai.micro.mq.producer.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import com.hai.micro.mq.producer.config.RocketMqProducerConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * @Desc: 优雅关闭 处理容器关闭事件
 * @author: evan
 * @date: 2021-11-19
 */
@Component
@Slf4j
public class GracefulShutdownListener implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    private RocketMqProducerConfig rocketMqProducerConfig;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("Mq Producer ContextClosedEvent graceful shutdown start");
        rocketMqProducerConfig.getProducer().shutdown();
        log.info("Mq Producer ContextClosedEvent graceful shutdown end");
    }
}
