package com.hai.micro.common.token.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hai.micro.common.token.handler.FeignAuthInterceptor;
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

    @Bean
    public TokenAuthHandlerInterceptor getTokenAuthHandlerInterceptor() {
        return new TokenAuthHandlerInterceptor();
    }

    @Bean
    public FeignAuthInterceptor getFeignAuthInterceptor() {
        return new FeignAuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getFeignAuthInterceptor()).addPathPatterns("/**").order(Ordered.HIGHEST_PRECEDENCE);
        registry.addInterceptor(getTokenAuthHandlerInterceptor()).addPathPatterns("/**").order(Ordered.LOWEST_PRECEDENCE);
    }
}
