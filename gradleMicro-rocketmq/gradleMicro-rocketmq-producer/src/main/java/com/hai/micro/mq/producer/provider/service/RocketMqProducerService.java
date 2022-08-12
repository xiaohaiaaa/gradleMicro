package com.hai.micro.mq.producer.provider.service;

import com.hai.micro.common.other.bo.MqMessageInfo;

/**
 * @ClassName RocketMqProduceService
 * @Description mq消息提供service
 * @Author ZXH
 * @Date 2022/8/12 15:45
 * @Version 1.0
 **/
public interface RocketMqProducerService {

    /**
     * 发mq消息
     *
     * @param mqMessageInfo
     */
    void sendMsg(MqMessageInfo mqMessageInfo);
}
