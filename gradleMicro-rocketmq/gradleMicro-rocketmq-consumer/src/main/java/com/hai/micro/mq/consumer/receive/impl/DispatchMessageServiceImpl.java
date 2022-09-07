package com.hai.micro.mq.consumer.receive.impl;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hai.micro.common.other.bo.MqMessageInfo;
import com.hai.micro.common.other.nacos.NacosCommonConfig;
import com.hai.micro.mq.consumer.bo.BaseSubscribeBO;
import com.hai.micro.mq.consumer.config.SubscribeMessageInitConfig;
import com.hai.micro.mq.consumer.handle.CommDispatchApiService;
import com.hai.micro.mq.consumer.receive.DispatchMessageService;
import com.hai.micro.mq.consumer.utils.HeaderParamsUtils;

import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.Target;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName DispatchMessageService
 * @Description 消息分发实现类
 * @Author ZXH
 * @Date 2022/8/11 18:01
 * @Version 1.0
 **/
@Slf4j
@Service
public class DispatchMessageServiceImpl implements DispatchMessageService {

    @Autowired
    private SubscribeMessageInitConfig subscribeMessageInitConfig;
    @Autowired
    private NacosCommonConfig nacosCommonConfig;

    @Value("${spring.application.name}")
    private String applicationName;

    private CommDispatchApiService commDispatchApiService;

    @Autowired
    public void setCommDispatchApiService() {
        //HttpMessageConverter<Object> jsonConverter = new MappingJackson2HttpMessageConverter(new ObjectMapper());
        //ObjectFactory<HttpMessageConverters> converter = ()-> new HttpMessageConverters(jsonConverter);
        this.commDispatchApiService = Feign.builder().contract(new feign.Contract.Default())
            // 连接超时30秒，读取超时60秒
            .options(new Request.Options(30 * 1000, TimeUnit.MILLISECONDS, 60 * 1000, TimeUnit.MILLISECONDS, true))
            // 发生IO异常重试5次，每次重试最小间隔100ms，最大间隔1s，随着重试次数递增
            .retryer(new Retryer.Default()).encoder(new JacksonEncoder()).decoder(new JacksonDecoder())
            // 注册feign
            .target(Target.EmptyTarget.create(CommDispatchApiService.class));
    }

    @Override
    public void dispatchMessage(String message) {
        MqMessageInfo mqMessageInfo = JSONObject.parseObject(message, MqMessageInfo.class);
        List<BaseSubscribeBO> serviceSubscribes = subscribeMessageInitConfig.getServiceSubscribes();
        String topic = mqMessageInfo.getTopic();
        String tag = mqMessageInfo.getTag();
        // 遍历消息配置列表
        for (BaseSubscribeBO subscribeBO : serviceSubscribes) {
            if (subscribeBO.getTopic().equalsIgnoreCase(topic) && subscribeBO.getTags().contains(tag)) {
                // 调用服务接口
                log.info("call callback api :{},{},{}", mqMessageInfo.getMsgId(), subscribeBO.getCallbackApi(),
                    subscribeBO.getServiceName());
                try {
                    Map<String, String> tokenHeaders = HeaderParamsUtils
                        .buildServiceTokenHeaders(nacosCommonConfig.getServiceAccessSecrets(), applicationName);
                    commDispatchApiService.call(new URI(subscribeBO.getCallbackApi()), mqMessageInfo, tokenHeaders);
                } catch (Exception e) {
                    log.error("dispatchMessage failed : {} ", mqMessageInfo.getMsgId(), e);
                }
            }
        }
    }
}
