package com.hai.micro.common.token.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hai.micro.common.other.constant.BizConstants;
import com.hai.micro.common.other.constant.FeignAuthContext;
import com.hai.micro.common.other.error.BusinessException;
import com.hai.micro.common.other.nacos.NacosCommonConfig;
import com.hai.micro.common.other.utils.FeignSecretUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName TokenAuthService
 * @Description token拦截校验
 * @Author ZXH
 * @Date 2022/7/18 10:37
 * @Version 1.0
 **/
@Slf4j
@Service
public class FeignTokenAuthService {

    @Autowired
    private NacosCommonConfig nacosCommonConfig;

    public void checkServiceToken(String serviceName, String serviceToken, String serviceNonce){
        if (!nacosCommonConfig.getServiceAccessEnable()) {
            return;
        }
        List<String> tokenList = Arrays.stream(serviceToken.split(BizConstants.CHAR_COMMA)).collect(Collectors.toList());
        boolean checkSign = false;
        for (String token : nacosCommonConfig.getServiceAccessSecrets()) {
            if (tokenList.contains(FeignSecretUtils.buildToken(serviceName, serviceNonce, token))) {
                FeignAuthContext.setFeignAuth(true);
                checkSign = true;
                break;
            }
        }
        FeignAuthContext.setFeignAuth(true);
        if (!checkSign) {
            throw new BusinessException("抱歉暂无访问权限！");
        }
    }
}
