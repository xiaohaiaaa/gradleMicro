package com.hai.micro.service.test.listener;

import org.springframework.context.ApplicationEvent;

import com.hai.micro.service.test.entity.City;

/**
 * @ClassName TestEvent
 * @Description 测试事件监听和发布
 * @Author ZXH
 * @Date 2022/2/25 17:33
 * @Version 1.0
 **/
public class TestEventBO extends ApplicationEvent {

    private City city;

    public TestEventBO(Object source, City city) {
        super(source);
        this.city = city;
    }

    public City getCity() {
        return city;
    }
}
