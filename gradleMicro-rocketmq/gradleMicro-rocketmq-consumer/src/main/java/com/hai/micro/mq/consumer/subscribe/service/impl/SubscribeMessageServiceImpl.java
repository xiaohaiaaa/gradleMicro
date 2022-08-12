package com.hai.micro.mq.consumer.subscribe.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.hai.micro.mq.consumer.config.PropAutoRefreshBO;

import com.hai.micro.mq.consumer.bo.BaseSubscribeBO;
import com.hai.micro.mq.consumer.entity.MsgCallbackInfo;
import com.hai.micro.mq.consumer.entity.MsgSubscribeConfig;
import lombok.extern.slf4j.Slf4j;
import com.hai.micro.mq.consumer.subscribe.mapper.MsgCallbackInfoMapper;
import com.hai.micro.mq.consumer.subscribe.mapper.MsgSubscribeConfigMapper;
import com.hai.micro.mq.consumer.subscribe.service.SubscribeMessageService;

/**
 * @ClassName SubscribeMessageServiceImpl
 * @Description 消息分发配置实现类
 * @Author ZXH
 * @Date 2022/8/12 10:48
 * @Version 1.0
 **/
@Slf4j
public class SubscribeMessageServiceImpl implements SubscribeMessageService {

    @Autowired
    private MsgSubscribeConfigMapper msgSubscribeConfigMapper;
    @Autowired
    private MsgCallbackInfoMapper msgCallbackInfoMapper;
    @Autowired
    private PropAutoRefreshBO propAutoRefreshBO;

    @Override
    public List<BaseSubscribeBO> findAllSubscribes() {
        List<MsgSubscribeConfig> configs = msgSubscribeConfigMapper.listValid();
        List<MsgCallbackInfo> callbackInfos = msgCallbackInfoMapper.listValid();
        if (CollectionUtils.isEmpty(configs) || CollectionUtils.isEmpty(callbackInfos)) {
            log.info("findAllSubscribes 查询无可用配置");
            return Lists.newArrayList();
        }
        log.info("findAllSubscribes event.url | {}", propAutoRefreshBO.getEventCallbackBaseUrl());

        Map<String, List<MsgSubscribeConfig>> map =
                configs.stream().collect(Collectors.groupingBy(MsgSubscribeConfig::getServiceName));

        List<BaseSubscribeBO> result = Lists.newArrayList();
        BaseSubscribeBO bo;
        for (MsgCallbackInfo callbackInfo : callbackInfos) {
            List<MsgSubscribeConfig> configList = map.get(callbackInfo.getServiceName());
            if (Objects.nonNull(configList)) {
                for (MsgSubscribeConfig msgSubscribeConfig : configList) {
                    bo = new BaseSubscribeBO();
                    bo.setCallbackApi(propAutoRefreshBO.getEventCallbackBaseUrl() + callbackInfo.getCallbackApi());
                    bo.setServiceName(callbackInfo.getServiceName());
                    bo.setTopic(msgSubscribeConfig.getTopicName());
                    bo.setTags(StringUtils.isNotBlank(msgSubscribeConfig.getTagName())
                            ? Arrays.asList(msgSubscribeConfig.getTagName().split("\\|")) : Lists.newArrayList());
                    result.add(bo);
                }
            }
        }
        return result;
    }
}
