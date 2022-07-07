package com.hai.micro.service.test.sharding;

import java.util.Collection;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import com.alibaba.fastjson.JSON;
import com.hai.micro.service.test.constant.ShardingConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName TradeShardingTableAlgorithmDev
 * @Description
 * @Author ZXH
 * @Date 2022/5/16 15:24
 * @Version 1.0
 **/
@Slf4j
public class TradeShardingTableAlgorithmDev implements PreciseShardingAlgorithm<String> {

    /**
     * availableTargetNames传过来的是表名集合，shardingValue传过来的是分表键值
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
            if (db.endsWith(String.valueOf(Integer.parseInt(shardingValue.getValue()) % ShardingConstant.TABLE_NUM_DEV))) {
                return db;
            }
        }
        return null;
    }
}
