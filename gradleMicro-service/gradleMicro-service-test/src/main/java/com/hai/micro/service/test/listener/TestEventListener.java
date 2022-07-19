package com.hai.micro.service.test.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.hai.micro.common.other.entity.City;
import com.hai.micro.service.test.firstTest.mapper.CityMapper;

/**
 * @ClassName TestEventListener
 * @Description 测试事件监听
 * @Author ZXH
 * @Date 2022/2/25 17:43
 * @Version 1.0
 **/
@Component
public class TestEventListener {

    @Autowired
    private CityMapper cityMapper;

    @TransactionalEventListener(classes = TestEventBO.class, phase = TransactionPhase.BEFORE_COMMIT)
    public void testEventListener(TestEventBO event) {
        City city = event.getCity();
        cityMapper.insert(city);
    }
}
