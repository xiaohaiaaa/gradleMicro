package com.hai.micro.common.other.aspect;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.hai.micro.common.other.anno.NoCheckSign;
import com.hai.micro.common.other.constant.AuthConstants;
import com.hai.micro.common.other.enums.CheckSignEnum;
import com.hai.micro.common.other.error.BaseErrorCode;
import com.hai.micro.common.other.error.BusinessException;
import com.hai.micro.common.other.nacos.NacosCommonConfig;
import com.hai.micro.common.other.request.RequestBodyContext;
import com.hai.micro.common.other.request.RequestWrapper;
import com.hai.micro.common.other.utils.GsonUtil;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.HmacAlgorithm;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName CheckSign
 * @Description 签名校验
 * @Author ZXH
 * @Date 2021/12/8 18:13
 * @Version 1.0
 **/
@Aspect
@Component
@Order(0)
@Slf4j
public class CheckSign {

    @Autowired
    private NacosCommonConfig nacosCommonConfig;

    @Pointcut("execution(* com.hai.micro.service..*Controller.*(..))")
    private void pointcut() {
    }

    @Before("pointcut()")
    public void checkSign(JoinPoint joinPoint) {
        this.handle(joinPoint);
    }

    private void handle(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
        Method method = methodSignature.getMethod();
        NoCheckSign noCheckSign = method.getAnnotation(NoCheckSign.class);
        // 标注了不需要签名的注解，不校验
        if (noCheckSign != null) {
            return;
        }
        RequestWrapper requestWrapper = RequestBodyContext.REQUEST_BODY.get();
        // 线上运维不校验
        if (nacosCommonConfig.getDevOpsReqHeader().equals(requestWrapper.getHeader(AuthConstants.DEV_OPS))) {
            return;
        }
        // 开关关闭不校验
        if (CheckSignEnum.close.name().equals(nacosCommonConfig.getParamSignCheckMode())) {
            return;
        }
        String sign = requestWrapper.getHeader(AuthConstants.APP_SIGN);
        String appId = requestWrapper.getHeader(AuthConstants.APP_ID);
        String timestamp = requestWrapper.getHeader(AuthConstants.APP_TIMESTAMP);
        String nonce = requestWrapper.getHeader(AuthConstants.APP_NONCE);
        // 兼容模式，签名参数为空不校验
        if (CheckSignEnum.compatible.name().equals(nacosCommonConfig.getParamSignCheckMode())) {
            if (Strings.isBlank(sign) || Strings.isBlank(appId) || Strings.isBlank(timestamp) || Strings.isBlank(nonce)) {
                return;
            }
        }
        // 签名为空，报错
        if (Strings.isBlank(sign)) {
            throw new BusinessException(BaseErrorCode.SIGN_EMPTY_ERROR);
        } else {
            // 签名过期，报错
            if (Long.parseLong(timestamp) + nacosCommonConfig.getParamSignTimeStamp() < System.currentTimeMillis()) {
                throw new BusinessException(BaseErrorCode.SIGN_OVERDUE_ERROR);
            }
        }
        // 取POST请求参数
        String bodyParam = null;
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
            if (requestBody != null) {
                bodyParam = requestWrapper.getBody();
                break;
            }
        }
        // 取GET请求参数
        Map<String, String[]> queryParam = requestWrapper.getParameterMap();
        // 校验appId对应枚举值
        String appIdParam = JSONObject.parseObject(nacosCommonConfig.getAppJwtId()).getString(appId);
        if (Strings.isBlank(appIdParam)) {
            // appId不存在，报错
            throw new BusinessException(BaseErrorCode.SIGN_CHECK_ERROR);
        } else {
            // 验签
            String paramSign = createParamSign(queryParam, bodyParam, appId, timestamp, nonce, appIdParam);
            if (!Objects.equals(paramSign, sign)) {
                throw new BusinessException(BaseErrorCode.SIGN_CHECK_FAIL);
            }
        }
    }

    /**
     * 生产签名参数
     * @param queryParam
     * @param bodyParam
     * @param appId
     * @param timestamp
     * @param nonce
     * @param appIdParam
     * @return
     */
    private String createParamSign(Map<String, String[]> queryParam, String bodyParam, String appId, String timestamp,
                                   String nonce, String appIdParam) {
        Map<String, String> paramMap = new TreeMap<>();
        paramMap.put(AuthConstants.APP_ID, appId);
        paramMap.put(AuthConstants.APP_TIMESTAMP, timestamp);
        paramMap.put(AuthConstants.APP_NONCE, nonce);
        // 如果POST请求参数不为空，加入paramMap里面
        if (Strings.isNotBlank(bodyParam)) {
            Gson gson = GsonUtil.getGsonInstance();
            String bodyStr = gson.toJson(gson.fromJson(bodyParam, SortedMap.class), LinkedHashMap.class);
            paramMap.put("requestBody", bodyStr);
        }
        // 如果Get请求参数不为空，加入paramMap里面
        if (queryParam != null && queryParam.size() > 0) {
            for(Map.Entry<String, String[]> entry : queryParam.entrySet()) {
                paramMap.put(entry.getKey(), entry.getValue()[0]);
            }
        }
        // 构建生成签名的字符串
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<String, String> entry : paramMap.entrySet()) {
            stringBuilder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        String signStr = stringBuilder.substring(1);
        log.info("sign sort str:{}", signStr);
        return DigestUtil.hmac(HmacAlgorithm.HmacSHA1, appIdParam.getBytes(StandardCharsets.UTF_8)).digestHex(DigestUtil.md5(signStr));
    }
}
