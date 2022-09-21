package com.hai.micro.common.event.listen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.hai.micro.common.event.bo.BasePushBo;
import com.hai.micro.common.event.event.LocalMessageEvent;
import com.hai.micro.common.other.bo.MqMessageInfo;
import com.hai.micro.common.token.feign.FeignMqProducerClient;

import cn.hutool.core.util.IdUtil;

/**
 * @ClassName LocalMessageListen
 * @Description
 * @Author ZXH
 * @Date 2022/8/12 17:19
 * @Version 1.0
 **/
@Component
public class LocalMessageListen {

    @Autowired(required = false)
    private FeignMqProducerClient feignMqProducerClient;

    @EventListener(value = {LocalMessageEvent.class})
    public <T> void listenLocalMessage(LocalMessageEvent<T> localMessageEvent) {
        BasePushBo<T> basePushBo = localMessageEvent.getBasePushBo();
        //log.info("listen local message body: {}", basePushBo);
        switch (basePushBo.getType()) {
            case EVENT:
                // 推送消息到mq producer服务
                MqMessageInfo mqMessageInfo = new MqMessageInfo();
                mqMessageInfo.setMsgId(IdUtil.objectId());
                mqMessageInfo.setTopic(basePushBo.getTopic().name());
                mqMessageInfo.setTag(basePushBo.getTag().name());
                mqMessageInfo.setContent(JSON.toJSONString(basePushBo.getContent()));
                feignMqProducerClient.pushMsg(mqMessageInfo);
                break;
            default:
                //log.error("listen local message type no exist!");
                break;
        }
    }

}
