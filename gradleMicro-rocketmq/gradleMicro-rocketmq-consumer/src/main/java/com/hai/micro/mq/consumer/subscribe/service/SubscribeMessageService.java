package com.hai.micro.mq.consumer.subscribe.service;

import java.util.List;

import com.hai.micro.mq.consumer.bo.BaseSubscribeBO;


/**
 * @ClassName SubscribeMessageService
 * @Description: 消息分发配置service接口
 * @Author zxh
 * @Date 2022/08/12 10:30
 * @Version 1.0
 **/
public interface SubscribeMessageService {

    /**
     * 获取所有分发配置
     * 
     * @return {@link BaseSubscribeBO }
     */
    List<BaseSubscribeBO> findAllSubscribes();

}
