package com.hai.micro.common.other.utils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.hai.micro.common.other.constant.BizConstants;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;

/**
 * @ClassName FeignSecretUtils
 * @Description feign请求工具类
 * @Author ZXH
 * @Date 2022/7/15 15:03
 * @Version 1.0
 **/
public class FeignSecretUtils {

    /**
     * 微服务token生成
     *
     * @param applicationName
     * @param serviceNonce
     * @param serviceAccessSecrets
     * @return
     */
    public static String createServiceToken(String applicationName, String serviceNonce,
        List<String> serviceAccessSecrets) {
        StringBuilder sb = new StringBuilder();
        for (String secret : serviceAccessSecrets) {
            sb.append(buildToken(applicationName, serviceNonce, secret)).append(BizConstants.CHAR_COMMA);
        }
        return sb.toString();
    }

    /**
     * 微服务令牌生成
     *
     * @param applicationName
     * @param serviceNonce
     * @param secret
     * @return
     */
    public static String buildToken(String applicationName, String serviceNonce, String secret) {
        return SmUtil.sm4(secret.getBytes(StandardCharsets.UTF_8)).encryptBase64(applicationName + serviceNonce);
    }

}
