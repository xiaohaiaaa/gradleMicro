package com.hai.micro.common.other.request;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

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

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            RequestWrapper requestWrapper = null;
            if (!"/test/import/excel".equals(httpServletRequest.getRequestURI())) {
                requestWrapper = new RequestWrapper(httpServletRequest);
                RequestBodyContext.REQUEST_BODY.set(requestWrapper);
            }
            chain.doFilter(Objects.isNull(requestWrapper) ? request : requestWrapper, response);
        } finally {
            RequestBodyContext.REQUEST_BODY.remove();
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
