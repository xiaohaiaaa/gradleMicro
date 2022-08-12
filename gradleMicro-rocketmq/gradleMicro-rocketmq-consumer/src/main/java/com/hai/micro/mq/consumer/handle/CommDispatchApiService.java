package com.hai.micro.mq.consumer.handle;

import java.net.URI;
import java.util.Map;

import com.hai.micro.common.other.api.ResponseModel;

import com.hai.micro.common.other.bo.MqMessageInfo;
import feign.HeaderMap;
import feign.RequestLine;

/**
 * @ClassName CommDispatchApiService
 * @Description: rpc feign调用
 * @Author Liyr
 * @Date 2021/12/24 14:59
 * @Version 1.0
 **/
public interface CommDispatchApiService {

    /**
     * 回调 调用订阅事件服务的接口
     *
     * @param baseUri 回调地址
     * @param messageInfo messageInfo对象
     * @param headers 请求头
     * @return 字符串Boole结果
     */
    @RequestLine("POST")
    ResponseModel<Object> call(URI baseUri, MqMessageInfo messageInfo, @HeaderMap Map<String,String> headers);

}
