package com.hai.micro.common.token.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hai.micro.common.token.handler.TokenAuthHandlerInterceptor;

/**
 * @ClassName WebConfig
 * @Description 自定义添加拦截器配置
 * @Author ZXH
 * @Date 2022/7/1 16:23
 * @Version 1.0
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TokenAuthHandlerInterceptor tokenAuthHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenAuthHandlerInterceptor).addPathPatterns("/**");
    }
}
