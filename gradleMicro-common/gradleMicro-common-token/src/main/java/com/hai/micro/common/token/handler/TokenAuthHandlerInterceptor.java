package com.hai.micro.common.token.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.hai.micro.common.other.anno.WhiteList;
import com.hai.micro.common.other.constant.AuthConstants;
import com.hai.micro.common.other.constant.FeignAuthContext;
import com.hai.micro.common.other.constant.UserAuthContext;
import com.hai.micro.common.other.enums.PlatformAuthTypeEnum;
import com.hai.micro.common.other.enums.SystemAuthTypeEnum;
import com.hai.micro.common.other.error.BusinessException;
import com.hai.micro.common.other.nacos.NacosCommonConfig;
import com.hai.micro.common.other.utils.JwtUtils;
import com.hai.micro.common.other.utils.WebUtils;
import com.hai.micro.common.other.vo.JwtAccessTokenVO;
import com.hai.micro.common.token.service.ClientAuthService;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName TokenAuthHandlerInterceptor
 * @Description token校验
 * @Author ZXH
 * @Date 2022/5/17 15:21
 * @Version 1.0
 **/
@Slf4j
public class TokenAuthHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private NacosCommonConfig nacosCommonConfig;
    @Autowired
    private ClientAuthService clientAuthService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 配置白名单接口直接跳过
        if (nacosCommonConfig.getWhiteListStrs().contains(request.getRequestURI())) {
            return true;
        }
        // 白名单注解接口直接跳过
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            WhiteList whiteList = ((HandlerMethod) handler).getMethodAnnotation(WhiteList.class);
            if (whiteList != null) {
                return true;
            }
        }
        try {
            // 微服务feign授权通过，直接跳过
            if (FeignAuthContext.getFeignAuth()) {
                return true;
            }
            // token校验
            String accessToken = request.getHeader(AuthConstants.JWT_ACCESS_TOKEN);
            if (accessToken == null || "null".equals(accessToken)) {
                accessToken = null;
            }
            if (Strings.isBlank(accessToken)) {
                log.info("请求IP: {}", WebUtils.getIpServlet(request));
                throw new BusinessException("身份验证失败，token为空");
            } else {
                JwtAccessTokenVO jwtAccessTokenVO = JwtUtils.parseClientToken(accessToken);
                if (jwtAccessTokenVO == null) {
                    log.info("请求IP: {}", WebUtils.getIpServlet(request));
                    log.info("请求token: {}", accessToken);
                    throw new BusinessException("身份验证失败，请重新登陆");
                } else {
                    // 校验服务授权类型
                    SystemAuthTypeEnum systemAuthTypeEnum = verifySystemAuthType(jwtAccessTokenVO);
                    if (systemAuthTypeEnum.equals(SystemAuthTypeEnum.GATEWAY)) {

                    } else if (systemAuthTypeEnum.equals(SystemAuthTypeEnum.SERVICE)) {

                    } else if (systemAuthTypeEnum.equals(SystemAuthTypeEnum.CLIENT)) {
                        verifyPlatformAuthType(jwtAccessTokenVO);
                        clientAuthService.handler(handler, jwtAccessTokenVO);
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("token auth exception: ", e);
            UserAuthContext.remove();
            throw e;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserAuthContext.remove();
        FeignAuthContext.remove();
    }

    /**
     * 校验服务授权类型
     *
     * @param jwtAccessTokenVO
     * @return
     */
    private SystemAuthTypeEnum verifySystemAuthType(JwtAccessTokenVO jwtAccessTokenVO) {
        boolean verifyAuthType = false;
        SystemAuthTypeEnum systemAuthTypeEnum = SystemAuthTypeEnum.CLIENT;
        for (SystemAuthTypeEnum value : SystemAuthTypeEnum.values()) {
            if (value.name().equals(jwtAccessTokenVO.getServiceAuthType())) {
                verifyAuthType = true;
                systemAuthTypeEnum = value;
                break;
            }
        }
        if (!verifyAuthType) {
            throw new BusinessException("身份验证失败，服务授权错误");
        }
        return systemAuthTypeEnum;
    }

    /**
     * 校验平台类型
     *
     * @param jwtAccessTokenVO
     */
    private void verifyPlatformAuthType(JwtAccessTokenVO jwtAccessTokenVO) {
        boolean verifyAuthType = false;
        for (PlatformAuthTypeEnum value : PlatformAuthTypeEnum.values()) {
            if (value.name().equals(jwtAccessTokenVO.getPlatformAuthType())) {
                verifyAuthType = true;
                break;
            }
        }
        if (!verifyAuthType) {
            throw new BusinessException("身份验证失败，平台类型错误");
        }
    }
}
