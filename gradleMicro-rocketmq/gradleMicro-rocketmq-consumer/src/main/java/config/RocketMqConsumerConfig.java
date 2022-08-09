package config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import com.hai.micro.common.other.enums.BaseTopicEnum;

/**
 * @ClassName RocketMqProducerConfig
 * @Description mq消费者配置类
 * @Author ZXH
 * @Date 2022/7/21 18:19
 * @Version 1.0
 **/
@Component
public class RocketMqConsumerConfig {

    private static DefaultMQProducer producer;

    /**
     * 初始化mq生产者配置
     *
     * @throws MQClientException
     */
    @PostConstruct
    public void initProducer() throws MQClientException {
        // 实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("please_rename_unique_group_name");

        // 设置NameServer的地址
        consumer.setNamesrvAddr("localhost:9876");

        // 订阅一个或者多个Topic，以及Tag来过滤需要消费的消息
        List<String> listenerTopics = Arrays.stream(BaseTopicEnum.values()).map(Enum::name)
                .collect(Collectors.toList());
        for (String topic : listenerTopics) {
            consumer.subscribe(topic, "*");
        }

        // 注册回调以在消息到达时执行以进行并发消费
        consumer.registerMessageListener(new RocketMqConsumerListener());

        // 启动消费者
        consumer.start();
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
