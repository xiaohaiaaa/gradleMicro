package com.hai.micro.service.test.firstTest.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hai.micro.common.other.entity.OnlineTransLog;

/**
 * @ClassName OnlineTransLogMapper
 * @Description
 * @Author ZXH
 * @Date 2022/5/16 16:55
 * @Version 1.0
 **/
public interface OnlineTransLogMapper extends BaseMapper<OnlineTransLog> {

    /**
     * 根据商户订单号查询
     *
     * @param tradeNo
     * @return
     */
    OnlineTransLog getLogByTradeNo(@Param("tradeNo") String tradeNo, @Param("rootOrgId") String rootOrgId);
}
