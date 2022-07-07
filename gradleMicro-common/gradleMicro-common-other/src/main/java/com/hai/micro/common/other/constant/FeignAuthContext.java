package com.hai.micro.common.other.constant;

/**
 * @ClassName FeignAuthContext
 * @Description feign请求授权容器
 * @Author ZXH
 * @Date 2022/5/17 16:31
 * @Version 1.0
 **/
public class FeignAuthContext {

    /**
     * feign请求授权结果
     */
    private static ThreadLocal<Boolean> feignReqAuth = new ThreadLocal<>();

    /**
     * feign请求授权结果
     *
     * @return
     */
    public static Boolean getFeignAuth() {
        if (feignReqAuth == null || feignReqAuth.get() == null) {
            return false;
        }
        return feignReqAuth.get();
    }

    /**
     * 保存feign请求授权结果
     *
     * @param judge
     */
    public static void setFeignAuth(Boolean judge) {
        feignReqAuth.set(judge);
    }

    /**
     * 释放ThreadLocal空间
     */
    public static void remove() {
        feignReqAuth.remove();
    }
}
