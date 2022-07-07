package com.hai.micro.gateway.utils;


import org.apache.poi.ss.formula.functions.T;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;

import com.alibaba.fastjson.JSON;
import com.hai.micro.common.other.api.ResponseModel;
import com.hai.micro.common.other.error.IErrorCode;

import reactor.core.publisher.Mono;

/**
 * @ClassName ResponseUtil
 * @Description 请求相应工具
 * @Author ZXH
 * @Date 2022/6/2 15:46
 * @Version 1.0
 **/
public class ResponseUtil {

    /**
     * 设置webflux模型响应
     *
     * @param response
     * @param value
     * @param errorCode
     * @return
     */
    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, String value, IErrorCode errorCode) {
        response.setStatusCode(HttpStatus.OK);
        ResponseModel<T> responseModel = null;
        if (errorCode != null) {
            responseModel = ResponseModel.fail(errorCode);
        } else {
            responseModel = ResponseModel.fail(value);
        }
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSON.toJSONBytes(responseModel));
        return response.writeWith(Mono.just(dataBuffer));
    }
}
