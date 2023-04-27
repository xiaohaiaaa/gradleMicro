package com.hai.micro.common.other.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @Desc: spring 容器工具类
 * @author: zxh
 * @date: 2023-04-27
 */
@Component
@Slf4j
public class SpringContextUtil implements ApplicationContextAware {

    /**
     * spring context
     */
    private static ApplicationContext applicationContext;

    /**
     * 获取上下文环境，代理对象
     *
     * @param className
     * @param <T>
     * @return
     * @throws BeansException
     */
    public static <T> T getBean(Class<T> className) throws BeansException {
        return applicationContext.getBean(className);
    }

    /**
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 实现ApplicationContextAware接口的回调方法。设置上下文环境
     *
     * @param applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        log.info("注入容器上下文");
        SpringContextUtil.applicationContext = applicationContext;
    }
}
