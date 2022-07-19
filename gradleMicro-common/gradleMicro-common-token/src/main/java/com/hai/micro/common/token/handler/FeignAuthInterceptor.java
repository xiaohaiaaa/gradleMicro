package com.hai.micro.common.token.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.hai.micro.common.other.constant.CloudFeignConstants;
import com.hai.micro.common.other.constant.FeignAuthContext;
import com.hai.micro.common.token.service.FeignTokenAuthService;

/**
 * @ClassName FeignAuthInterceptor
 * @Description feign请求拦截器校验
 * @Author ZXH
 * @Date 2022/7/18 10:13
 * @Version 1.0
 **/
@Component
public class FeignAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private FeignTokenAuthService feignTokenAuthService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String serviceName = request.getHeader(CloudFeignConstants.SERVICE_NAME);
        String serviceToken = request.getHeader(CloudFeignConstants.SERVICE_SECRETS);
        String serviceNonce = request.getHeader(CloudFeignConstants.SERVICE_NONCE);
        if (Strings.isNotBlank(serviceName) && Strings.isNotBlank(serviceToken) && Strings.isNotBlank(serviceNonce)) {
            feignTokenAuthService.checkServiceToken(serviceName, serviceToken, serviceNonce);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        FeignAuthContext.remove();
    }
}
