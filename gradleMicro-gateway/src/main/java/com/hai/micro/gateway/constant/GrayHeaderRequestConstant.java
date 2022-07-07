package com.hai.micro.gateway.constant;

/**
 * @ClassName GrayHeaderRequestConstant
 * @Description 灰度请求头常量
 * @Author ZXH
 * @Date 2022/5/31 11:14
 * @Version 1.0
 **/
public final class GrayHeaderRequestConstant {

    /**
     * 灰度请求头
     */
    public static String GRAY_HEADER_FLAG = "x-header-flag";
    /**
     * 根据租户用户灰度
     */
    public static String GRAY_TENANT_USER_HEADER = "x-tenant-user-id";
    /**
     * 根据门锁租户灰度
     */
    public static String GRAY_DOOR_TENANT_HEADER = "x-door-tenant-id";
    /**
     * 根据商品租户灰度
     */
    public static String GRAY_GOODS_TENANT_HEADER = "x-goods-tenant-id";
    /**
     * 根据开放平台用户灰度
     */
    public static String GRAY_OPEN_APP_HEADER = "x-open-app-id";
    /**
     * 根据机器灰度
     */
    public static String GRAY_MACHINE_HEADER = "x-machine-id";
    /**
     * 根据机器版本灰度
     */
    public static String GRAY_MACHINE_VERSION = "x-machine-version";

}
