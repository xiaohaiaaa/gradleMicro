package com.hai.micro.mq.consumer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@RefreshScope
@Data
public class PropAutoRefreshBO {

    /**
     * 异步事件基础调用域名地址
     */
    @Value("${event.callback.base.url:http://localhost:9000}")
    private String eventCallbackBaseUrl;

    /**
     * 是否刷新分发配置
     */
    @Value("${refresh.com.hai.micro.mq.consumer.subscribe.config:false}")
    private Boolean refreshSubscribeConfig;

    /**
     * 本地消息事物重试数量
     */
    @Value("${local.msg.compensate.num:50}")
    private Integer localMsgCompensateNum;

    /**
     * 本地消息事物重试间隔分钟 默认5分钟之类
     */
    @Value("${local.msg.compensate.minutes:5}")
    private Integer localMsgCompensateMinutes;

    /**
     * 本地消息事物重试最大次数 默认最大5次
     */
    @Value("${local.msg.compensate.max.retry:5}")
    private Integer localMsgCompensateMaxRetry;

}
