package com.hai.micro.common.other.enums;

/**
 * @ClassName JwtAppIdEnum
 * @Description 应用JWT类型
 * @Author ZXH
 * @Date 2022/5/23 17:05
 * @Version 1.0
 **/
public enum JwtAppIdEnum {
    /**
     * 管理端 以MANAGE开头
     */
    MANAGE,

    /**
     * AI管理系统
     */
    MANAGE_AI_PC,

    /**
     * 租户PC管理系统
     */
    MANAGE_TENANT_PC,

    /**
     * 客服识别系统
     */
    MANAGE_CUSTOMER_PC,

    /**
     * 工厂测试应用
     */
    MANAGE_FACTORY_WX,

    /**
     * Android 客户端
     */
    ANDROID,

    /**
     * 网页应用 小程序+H5
     */
    WAP,

    /**
     * 对外开放平平台
     */
    OPEN;

}
