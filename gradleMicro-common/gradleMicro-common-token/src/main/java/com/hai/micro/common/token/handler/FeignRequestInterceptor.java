package com.hai.micro.common.token.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hai.micro.common.other.constant.CloudFeignConstants;
import com.hai.micro.common.other.nacos.NacosCommonConfig;
import com.hai.micro.common.other.utils.FeignSecretUtils;

import cn.hutool.core.util.RandomUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName FeignRequestInterceptor
 * @Description feign请求拦截器，设置请求头。
 * 交给spring管理之后，feign请求的时候会自动先调这个拦截器，不需要单独添加到webConfig
 * @Author ZXH
 * @Date 2022/7/15 14:51
 * @Version 1.0
 **/
@Slf4j
@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Autowired
    private NacosCommonConfig nacosCommonConfig;

    @Override
    public void apply(RequestTemplate template) {
        log.info("feign request url: {},  method: {}", template.feignTarget().url(), template.methodMetadata().configKey());

        String applicationName = nacosCommonConfig.getApplicationName();
        Boolean serviceAccessEnable = nacosCommonConfig.getServiceAccessEnable();
        List<String> serviceAccessSecrets = nacosCommonConfig.getServiceAccessSecrets();

        if (serviceAccessEnable) {
            try {
                String serviceNonce = RandomUtil.randomString(8);
                String serviceToken = FeignSecretUtils.createServiceToken(applicationName, serviceNonce, serviceAccessSecrets);
                template.header(CloudFeignConstants.SERVICE_NAME, applicationName);
                template.header(CloudFeignConstants.SERVICE_NONCE, serviceNonce);
                template.header(CloudFeignConstants.SERVICE_SECRETS, serviceToken);
            } catch (Exception e) {
                log.info("生成微服务Token失败:{}", e.getMessage());
            }
        }
    }
}
