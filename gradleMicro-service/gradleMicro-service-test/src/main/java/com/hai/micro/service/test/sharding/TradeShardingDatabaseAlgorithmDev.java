package com.hai.micro.service.test.sharding;

import java.util.Collection;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import com.alibaba.fastjson.JSON;
import com.hai.micro.service.test.constant.ShardingConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName TradeShardingDatabaseAlgorithmDev
 * @Description sharding分库规则
 * @Author ZXH
 * @Date 2022/5/16 14:53
 * @Version 1.0
 **/
@Slf4j
public class TradeShardingDatabaseAlgorithmDev implements PreciseShardingAlgorithm<String> {

    /**
     * availableTargetNames传过来的是库名集合，shardingValue传过来的是分库键值
     *
     * @param availableTargetNames
     * @param shardingValue
     * @return
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        log.debug("availableTargetNames:{},shardingValue:{}", JSON.toJSONString(availableTargetNames),
                JSON.toJSON(shardingValue));
        for (String db : availableTargetNames) {
            if (db.endsWith(String.valueOf(Integer.parseInt(shardingValue.getValue()) % ShardingConstant.DB_NUM_DEV))) {
                return db;
            }
        }
        return null;
    }
}
