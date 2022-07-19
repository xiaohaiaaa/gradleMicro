package com.hai.micro.service.test.firstTest.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hai.micro.common.other.entity.TheOrder;
import com.hai.micro.service.test.firstTest.mapper.TheOrderMapper;
import com.hai.micro.service.test.firstTest.service.OrderService;

/**
 * 订单信息
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TheOrderMapper orderMapper;

    //生成订单
    @Override
    public void createOrder(TheOrder theOrder) {
        orderMapper.insert(theOrder);
    }
}
