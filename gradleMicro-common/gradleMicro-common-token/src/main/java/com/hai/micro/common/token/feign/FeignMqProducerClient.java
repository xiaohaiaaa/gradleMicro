package com.hai.micro.common.token.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.hai.micro.common.other.api.ResponseModel;
import com.hai.micro.common.other.bo.MqMessageInfo;
import com.hai.micro.common.other.entity.City;
import com.hai.micro.common.token.handler.FeignAuthInterceptor;

/**
 * @ClassName FeignClient
 * @Description feign客户端
 * @Author ZXH
 * @Date 2022/7/15 11:08
 * @Version 1.0
 **/
@FeignClient(name = "gradleMicro-rocketmq-producer", path = "/service/mq/producer/common/feign", configuration = FeignAuthInterceptor.class)
public interface FeignMqProducerClient {

    /**
     * mq消息发送
     *
     * @param mqMessageInfo
     * @return
     */
    @PostMapping("v1/push")
    void pushMsg(@RequestBody MqMessageInfo mqMessageInfo);
}
