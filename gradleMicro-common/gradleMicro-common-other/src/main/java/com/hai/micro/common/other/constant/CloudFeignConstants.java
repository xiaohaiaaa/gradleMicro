package com.hai.micro.common.other.constant;

/**
 * @ClassName CloudFeignConstants
 * @Description feign常量
 * @Author ZXH
 * @Date 2022/7/18 9:56
 * @Version 1.0
 **/
public class CloudFeignConstants {

    /**
     * Prefix of {@link CloudFeignProperties}.
     */
    public static final String PROPERTY_PREFIX = "hai.cloud.feign";

    /**
     * 微服务加密-签名
     */
    public static final String SERVICE_SECRETS = "Service-Secrets";

    /**
     * 微服务加密-随机数
     */
    public static final String SERVICE_NONCE = "Service-Nonce";

    /**
     * 微服务加密-服务名称
     */
    public static final String SERVICE_NAME = "Service-Name";


    private CloudFeignConstants() {
        throw new AssertionError("Must not instantiate constant utility class");
    }

}
