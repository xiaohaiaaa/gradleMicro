package com.hai.micro.service.test.firstTest.service;

public interface MQStockService {

    /**
     * 监听库存消息队列，并消费
     * @param stockName
     */
    void decrByStockName(String stockName);
}
