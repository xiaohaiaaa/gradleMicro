package com.hai.micro.service.test.firstTest.service;

import com.hai.micro.common.other.entity.TheOrder;

public interface OrderService {

    //生成订单
    void createOrder(TheOrder theOrder);
}
