package com.hai.micro.service.test.firstTest.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.hai.micro.service.test.entity.OnlineTransLog;
import com.hai.micro.service.test.firstTest.mapper.OnlineTransLogMapper;
import com.hai.micro.service.test.firstTest.service.OnlineTransLogService;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName OnlineTransLogServiceImpl
 * @Description 交易流水业务实现类
 * @Author ZXH
 * @Date 2022/5/16 16:51
 * @Version 1.0
 **/
@Service
@Slf4j
public class OnlineTransLogServiceImpl implements OnlineTransLogService {

    @Autowired
    private OnlineTransLogMapper onlineTransLogMapper;

    @Override
    @DS("trade_sharding")
    public OnlineTransLog getLogByTradeNo(String tradeNo, String rootOrgId) {
        return onlineTransLogMapper.getLogByTradeNo(tradeNo, rootOrgId);
    }
}
