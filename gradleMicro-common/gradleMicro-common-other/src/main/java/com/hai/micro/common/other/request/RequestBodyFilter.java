package com.hai.micro.common.other.request;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.hai.micro.common.other.nacos.NacosCommonConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName RequestBodyFilter
 * @Description RequestBody过滤器
 * @Author ZXH
 * @Date 2021/12/8 20:07
 * @Version 1.0
 **/
@Slf4j
@Component
public class RequestBodyFilter implements Filter {

    private final NacosCommonConfig nacosCommonConfig;

    public RequestBodyFilter(NacosCommonConfig nacosCommonConfig) {
        this.nacosCommonConfig = nacosCommonConfig;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            RequestWrapper requestWrapper = null;
            if (!nacosCommonConfig.getWhiteApiRequestBody().contains(httpServletRequest.getRequestURI())) {
                requestWrapper = new RequestWrapper(httpServletRequest);
                RequestBodyContext.REQUEST_BODY.set(requestWrapper);
            }
            if(requestWrapper == null) {
                chain.doFilter(request, response);
            } else {
                chain.doFilter(requestWrapper, response);
            }
        } finally {
            RequestBodyContext.REQUEST_BODY.remove();
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
