package com.hai.micro.mq.consumer.handle;

import java.net.URI;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

import com.hai.micro.common.other.bo.MqMessageInfo;

import feign.HeaderMap;
import feign.Headers;
import feign.RequestLine;

/**
 * @ClassName CommDispatchApiService
 * @Description: rpc feign调用
 * @Author zxh
 * @Date 2022/09/06 14:59
 * @Version 1.0
 **/
public interface CommDispatchApiService {

    /**
     * 回调 调用订阅事件服务的接口
     *
     * @param baseUri 回调地址
     * @param messageInfo messageInfo对象
     * @param headers 请求头
     */
    @RequestLine("POST")
    @Headers({"Content-Type: application/json"})
    void call(URI baseUri, @RequestBody MqMessageInfo messageInfo, @HeaderMap Map<String,String> headers);

}
