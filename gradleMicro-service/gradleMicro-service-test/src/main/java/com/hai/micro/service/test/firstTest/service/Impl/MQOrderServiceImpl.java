package com.hai.micro.service.test.firstTest.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hai.micro.service.test.entity.TheOrder;
import com.hai.micro.service.test.firstTest.service.MQOrderService;
import com.hai.micro.service.test.firstTest.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MQOrderServiceImpl implements MQOrderService {

    @Autowired
    private OrderService orderService;

    /**
     * 监听订单消息队列，并消费
     * @param theOrder
     */
    //@RabbitListener(queues = MyRabbitMQConfig.ORDER_QUEUE)
    @Override
    public void creatOrder(TheOrder theOrder) {
        log.info("收到订单消息，订单用户为：{}，商品名称为：{}", theOrder.getOrderUser(), theOrder.getOrderName());
        //调用数据库创建订单信息
        orderService.createOrder(theOrder);
    }
}
