package com.hai.micro.common.other.enums;

/**
 * @ClassName SystemAuthTypeEnum
 * @Description 系统授权类型
 * @Author ZXH
 * @Date 2022/5/23 17:03
 * @Version 1.0
 **/
public enum SystemAuthTypeEnum {

    /**
     * 终端调用
     */
    CLIENT,
    /**
     * 服务互相调用
     * Feign调用
     */
    SERVICE,
    /**
     * 网关调用服务
     * 例如XXL-JOB
     */
    GATEWAY;

}
