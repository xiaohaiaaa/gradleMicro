package com.hai.micro.service.test.firstTest.service.Impl;

import static com.hai.micro.service.test.config.MyRabbitMQConfig.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hai.micro.common.other.entity.TheOrder;
import com.hai.micro.common.other.service.RedisService;
import com.rabbitmq.client.*;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SeckillServiceImpl {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RedisService redisService;

    public String seckillStock(String userName, String stockName) {
        Long decrResult = redisService.decrByKey(stockName);
        log.info("decrResult的结果为：{}", decrResult);
        if (decrResult >= 0) {
            log.info("用户：{}秒杀该商品：{}库存有余，可以进行下单操作", userName, stockName);
            //发消息给库存消息队列，将商品库存数量减一
            rabbitTemplate.convertAndSend(STORY_EXCHANGE, STORY_ROUTING_KEY, stockName);
            //发消息给订单消息队列，创建订单
            TheOrder theOrder = new TheOrder();
            theOrder.setOrderName(stockName);
            theOrder.setOrderUser(userName);
            rabbitTemplate.convertAndSend(ORDER_EXCHANGE, ORDER_ROUTING_KEY, theOrder);
            //用户下单成功
            return "用户：" + userName + "秒杀：" + stockName + "成功";
        } else {
            log.info("用户：{}秒杀的商品：{}没有库存了，秒杀已结束", userName, stockName);
            return "用户：" + userName + "秒杀：" + "失败，商品已抢完";
        }
    }

    /**
     * 生产者
     * @throws IOException
     * @throws TimeoutException
     */
    public void queueConfirmListen() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        //创建一个连接
        Connection connection = factory.newConnection();
        //创建一个通道
        Channel channel= connection.createChannel();
        //指定一个队列
        channel.queueDeclare("confirmQueue", false, false, false, null);
        //开启confirm消息确认机制
        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener() {
            //消息发送成功的回调
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("消息发送成功...");
            }

            //消息发送失败的回调
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("消息发送失败...执行相应的业务逻辑——重新发送...");
            }
        });
        // 维持异步调用，不能关闭连接。
        channel.basicPublish("", "confirmQueue", null,  "消息确认机制—confirm模式—异步".getBytes());
    }

    /**
     * 消费者
     * @throws IOException
     * @throws TimeoutException
     */
    public void queueConfirm() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        //创建一个连接
        Connection connection = factory.newConnection();
        //创建一个通道
        Channel channel = connection.createChannel();
        //指定一个队列
        channel.queueDeclare("confirmQueue", false, false, false, null);
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者接收的消息是：" + new String(body));
                channel.basicAck(envelope.getDeliveryTag(), false);
                /**
                 *  该方法的作用是：拒绝接收消息，服务器会拒绝指定了 delivery_tag 的所有未确认的消息
                 *  param1: deliveryTag，消息的id
                 *  param2: multiple，是否是批量
                 *      true: 批量，会拒绝所有 deliveryTag 小于当前deliveryTag的消息
                 *      false: 只会拒绝 deliveryTag 一致的消息
                 *  param3: requeue，
                 *      true：表示拒收消息后将消息重新放入队列
                 *      false：表示拒收消息后删除该消息
                 */
            }
        };
        channel.basicConsume("confirmQueue", false, defaultConsumer);
    }

}
