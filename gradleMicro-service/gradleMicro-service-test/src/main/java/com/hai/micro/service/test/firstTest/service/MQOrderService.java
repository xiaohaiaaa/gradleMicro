package com.hai.micro.service.test.firstTest.service;

import com.hai.micro.common.other.entity.TheOrder;

public interface MQOrderService {

    /**
     * 监听订单消息队列，并消费
     * @param theOrder
     */
    void creatOrder(TheOrder theOrder);
}
