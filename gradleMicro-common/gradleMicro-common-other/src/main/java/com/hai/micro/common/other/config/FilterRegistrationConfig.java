package com.hai.micro.common.other.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.hai.micro.common.other.nacos.NacosCommonConfig;
import com.hai.micro.common.other.request.RequestBodyFilter;

/**
 * @ClassName FilterRegistrationConfig
 * @Description filter过滤器配置
 * @Author ZXH
 * @Date 2021/12/9 9:37
 * @Version 1.0
 **/
@Configuration
public class FilterRegistrationConfig {

    @Autowired
    private NacosCommonConfig nacosCommonConfig;

    @Bean
    public FilterRegistrationBean<RequestBodyFilter> filterRegist() {
        FilterRegistrationBean<RequestBodyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestBodyFilter(nacosCommonConfig));
        registrationBean.setName("requestBodyFilter");
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}
