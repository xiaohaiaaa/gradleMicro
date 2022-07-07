package com.hai.micro.common.other.request;


/**
 * @ClassName RequestBodyContext
 * @Description request请求上下文
 * @Author ZXH
 * @Date 2021/12/8 20:11
 * @Version 1.0
 **/
public class RequestBodyContext {

    public static ThreadLocal<RequestWrapper> REQUEST_BODY = new ThreadLocal<>();
}
