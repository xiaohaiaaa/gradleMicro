package com.hai.micro.mq.consumer.receive;

/**
 * @ClassName DispatchMessageService
 * @Description 消息分发service
 * @Author ZXH
 * @Date 2022/8/11 18:00
 * @Version 1.0
 **/
public interface DispatchMessageService {

    /**
     * 消息处理
     *
     * @param message
     *            消息体
     */
    void dispatchMessage(String message);
}
