package com.hai.micro.common.event.publish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.hai.micro.common.event.bo.BasePushBo;
import com.hai.micro.common.event.event.LocalMessageEvent;

/**
 * @ClassName LocalMessagePulibsh
 * @Description 本地消息发布
 * @Author ZXH
 * @Date 2022/8/12 17:12
 * @Version 1.0
 **/
@Component
public class LocalMessagePublish {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 消息发布
     *
     * @param basePushBo
     * @param <T>
     */
    public <T> void publish(BasePushBo<T> basePushBo) {
        applicationContext.publishEvent(new LocalMessageEvent<>(this, basePushBo));
    }
}
