package com.hai.micro.mq.consumer.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hai.micro.common.other.constant.AuthConstants;
import com.hai.micro.common.other.constant.CloudFeignConstants;
import com.hai.micro.common.other.utils.FeignSecretUtils;
import com.hai.micro.common.other.utils.JwtUtils;


/**
 * @ClassName HeaderParamsUtils
 * @Description: 构建请求头参数工具类
 * @Author zxh
 * @Date 2022/08/12 15:07
 * @Version 1.0
 **/
public class HeaderParamsUtils {

    /**
     * 本地缓存服务端请求token
     */
    protected static String accessToken;

    /**
     * 构建Header服务令牌参数
     *
     * @return
     */
    public static Map<String, String> buildServiceTokenHeaders(List<String> serviceAccessSecret, String applicationName) {
        Map<String, String> headers = new HashMap<>();
        String serviceNonce = String.valueOf(System.nanoTime());
        String serviceToken = FeignSecretUtils.createServiceToken(applicationName, serviceNonce, serviceAccessSecret);
        headers.put(CloudFeignConstants.SERVICE_SECRETS, serviceToken);
        headers.put(CloudFeignConstants.SERVICE_NAME, applicationName);
        headers.put(CloudFeignConstants.SERVICE_NONCE, serviceNonce);
        accessToken = JwtUtils.createServiceToken(applicationName);
        headers.put(AuthConstants.JWT_ACCESS_TOKEN, accessToken);
        return headers;
    }
}
