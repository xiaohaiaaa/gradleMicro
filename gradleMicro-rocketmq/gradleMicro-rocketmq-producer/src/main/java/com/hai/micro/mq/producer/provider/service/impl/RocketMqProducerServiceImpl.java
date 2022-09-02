package com.hai.micro.mq.producer.provider.service.impl;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hai.micro.common.other.bo.MqMessageInfo;
import com.hai.micro.mq.producer.config.RocketMqProducerConfig;
import com.hai.micro.mq.producer.provider.service.RocketMqProducerService;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName RocketMqProduceService
 * @Description mq消息生产实现类
 * @Author ZXH
 * @Date 2022/8/12 15:53
 * @Version 1.0
 **/
@Slf4j
@Service
public class RocketMqProducerServiceImpl implements RocketMqProducerService {

    @Autowired
    private RocketMqProducerConfig rocketMqProducerConfig;

    @Override
    public void sendMsg(MqMessageInfo mqMessageInfo) {
        try {
            StopWatch sw = new StopWatch();
            sw.start();
            Message message = new Message(mqMessageInfo.getTopic(), mqMessageInfo.getTag(),
                JSON.toJSONString(mqMessageInfo).getBytes(StandardCharsets.UTF_8));
            SendResult sendResult = rocketMqProducerConfig.getProducer().send(message);
            if (sendResult != null) {
                log.info("rocket.mq send msg success | local.msgId : {} ,topic : {} , result.msgId : {}",
                    mqMessageInfo.getMsgId(), mqMessageInfo.getTopic(), sendResult.getMsgId());
            }
            sw.stop();
            log.info("{} -> 单次生产耗时：{}", mqMessageInfo.getMsgId(), sw.getTime());
        } catch (Exception e) {
            log.error("rocket.mq send msg error | msgInfo : {} , ", JSON.toJSONString(mqMessageInfo), e);
        }
    }
}
