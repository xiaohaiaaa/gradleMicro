package com.hai.micro.mq.consumer.bo;

import java.util.List;

import lombok.Data;

/**
 * @ClassName BaseSubscribeBO
 * @Description: 消息分发处理BO
 * @Author zxh
 * @Date 2022/08/12 10:33
 * @Version 1.0
 **/
@Data
public class BaseSubscribeBO {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 回调地址
     */
    private String callbackApi;

    /**
     * 消息topic
     */
    private String topic;

    /**
     * 对应要分发的tag
     */
    private List<String> tags;
}
