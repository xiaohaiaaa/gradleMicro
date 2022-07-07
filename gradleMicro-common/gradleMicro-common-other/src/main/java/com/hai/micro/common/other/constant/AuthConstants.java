package com.hai.micro.common.other.constant;

/**
 * @ClassName AuthConstants
 * @Description 授权通用常量类
 * @Author ZXH
 * @Date 2021/12/9 9:30
 * @Version 1.0
 **/
public final class AuthConstants {

    /**
     * 请求头 访问token
     */
    public static final String JWT_ACCESS_TOKEN = "Hai-User-Access-Token";
    /**
     * 请求头 jwtAppId
     */
    public static final String APP_ID = "Hai-User-Access-App-Id";
    /**
     * 请求头 加密随机数
     */
    public static final String APP_NONCE = "Hai-User-Access-App-Nonce";
    /**
     * 请求头 时间戳
     */
    public static final String APP_TIMESTAMP = "Hai-User-Access-App-Timestamp";
    /**
     * 请求头 签名
     */
    public static final String APP_SIGN = "Hai-User-Access-App-Sign";
    /**
     * 请求头 运维
     */
    public static final String DEV_OPS = "Hai-Dev-Ops";
}
