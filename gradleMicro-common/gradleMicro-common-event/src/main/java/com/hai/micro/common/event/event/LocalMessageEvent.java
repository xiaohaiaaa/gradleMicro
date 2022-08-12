package com.hai.micro.common.event.event;

import org.springframework.context.ApplicationEvent;

import com.hai.micro.common.event.bo.BasePushBo;

import lombok.Getter;

/**
 * @ClassName LocalMessageEvent
 * @Description 本地消息事件
 * @Author ZXH
 * @Date 2022/8/12 17:02
 * @Version 1.0
 **/
@Getter
public class LocalMessageEvent<T> extends ApplicationEvent {

    private final BasePushBo<T> basePushBo;

    public LocalMessageEvent(Object source, BasePushBo<T> basePushBo) {
        super(source);
        this.basePushBo = basePushBo;
    }
}
