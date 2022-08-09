package com.hai.micro.service.test.listener;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.hai.micro.mq.producer.config.RocketMqProducerConfig;
import com.hai.micro.service.test.firstTest.service.TestService;
import com.hai.micro.service.test.utils.ThreadPoolUtil;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ContextCloseListener
 * @Description
 * @Author ZXH
 * @Date 2021/12/2 18:47
 * @Version 1.0
 **/
@Slf4j
@Component
public class ContextCloseListener implements ApplicationListener<ContextClosedEvent> {

    @Resource(name = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor executor;
    @Autowired
    private TestService testService;

    @SneakyThrows
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("检测到容器关闭事件!");
        log.info("准备关闭线程池!");
        //executor.shutdown();
        ThreadPoolUtil.shutdown();
        // awaitTermination需要在shutdown后使用，这样的话等线程池中任务都执行完毕之后会自动提前关闭，不必等满设置的等待时间
        Boolean nomal = ThreadPoolUtil.awaitTermination(120, TimeUnit.SECONDS);
        if (nomal) {
            System.out.println("线程池正常关闭!");
        } else {
            System.out.println("线程池超时关闭!");
        }
    }
}
