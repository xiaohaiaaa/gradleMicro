package com.hai.micro.mq.consumer.config;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.hai.micro.mq.consumer.receive.DispatchMessageService;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName RocketMqConsumerListener
 * @Description Rocketmq消费者处理对象
 * @Author ZXH
 * @Date 2022/8/9 18:24
 * @Version 1.0
 **/
@Slf4j
public class RocketMqConsumerListener implements MessageListenerConcurrently {

    private final DispatchMessageService dispatchMessageService;

    public RocketMqConsumerListener(DispatchMessageService dispatchMessageService) {
        this.dispatchMessageService = dispatchMessageService;
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        for (MessageExt msg : msgs) {
            String msgBody = new String(msg.getBody(), StandardCharsets.UTF_8);
            log.info("rocketMq get msg Id: {}, body: {}", msg.getMsgId(), msgBody);
            try {
                dispatchMessageService.dispatchMessage(msgBody);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } catch (Exception e) {
                log.error("rocketMq consume msg error: {}", e.getMessage(), e);
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
