package com.hai.micro.service.test.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * @ClassName DistributedIdUtil
 * @Description 分布式ID生成
 * @Author ZXH
 * @Date 2021/12/7 16:41
 * @Version 1.0
 **/
public class DistributedIdUtil {

    /**
     * 雪花算法ID - 19位
     * IdUtil.createSnowflake每次调用会创建一个新的Snowflake对象，不同的Snowflake对象创建的ID可能会有重复，因此请自行维护此对象为单例
     * @return
     */
    public static Long snowflakeId() {
        Snowflake snowflake = IdUtil.getSnowflake(1L, 1L);
        return snowflake.nextId();
    }

    /**
     * UUID - 32位
     * @return
     */
    public static String UUID() {
        return IdUtil.simpleUUID();
    }

    /**
     * objectId - 24位
     * @return
     */
    public static String ObjectId() {
        return IdUtil.objectId();
    }

}
