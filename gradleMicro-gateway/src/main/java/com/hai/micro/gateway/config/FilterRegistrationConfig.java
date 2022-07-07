package com.hai.micro.gateway.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hai.micro.gateway.filter.RequestBodyFilter;

/**
 * @ClassName FilterRegistrationConfig
 * @Description filter过滤器配置
 * @Author ZXH
 * @Date 2021/12/9 9:37
 * @Version 1.0
 **/
@Configuration
public class FilterRegistrationConfig {

    @Bean
    public FilterRegistrationBean<RequestBodyFilter> filterRegist() {
        FilterRegistrationBean<RequestBodyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestBodyFilter());
        registrationBean.setName("requestBodyFilter");
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
