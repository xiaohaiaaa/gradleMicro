package com.hai.micro.mq.producer.provider.rpc;

import javax.xml.ws.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hai.micro.common.other.api.ResponseModel;
import com.hai.micro.common.other.bo.MqMessageInfo;
import com.hai.micro.common.token.feign.FeignMqProducerClient;
import com.hai.micro.mq.producer.provider.service.RocketMqProducerService;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName RocketMqProducerController
 * @Description
 * @Author ZXH
 * @Date 2022/8/12 17:25
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/service/mq/producer/common/feign")
public class RocketMqProducerController implements FeignMqProducerClient {

    @Autowired
    private RocketMqProducerService rocketMqProducerService;

    @Override
    public void pushMsg(@RequestBody MqMessageInfo mqMessageInfo) {
        log.info("rocketMq producer msg: {}", mqMessageInfo);
        rocketMqProducerService.sendMsg(mqMessageInfo);
    }
}
