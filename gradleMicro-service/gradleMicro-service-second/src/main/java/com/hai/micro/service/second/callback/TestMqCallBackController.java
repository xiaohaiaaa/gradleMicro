package com.hai.micro.service.second.callback;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hai.micro.common.other.anno.NoCheckSign;
import com.hai.micro.common.other.anno.WhiteList;
import com.hai.micro.common.other.api.ResponseModel;
import com.hai.micro.common.other.bo.MqMessageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName TestMqCallBackImpl
 * @Description 测试MQ回调
 * @Author ZXH
 * @Date 2022/8/12 18:40
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("second/message/callback")
public class TestMqCallBackController {

    @WhiteList
    @NoCheckSign
    @PostMapping("v1")
    public ResponseModel testMqCallBack(@RequestBody MqMessageInfo callbackBO) {
        log.info("mq callback body: {}", callbackBO);
        return ResponseModel.success();
    }
}
