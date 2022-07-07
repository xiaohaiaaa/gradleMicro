package com.hai.micro.common.token.handler;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;

import com.alibaba.fastjson.JSONObject;
import com.hai.micro.common.other.anno.MachineApiAuth;
import com.hai.micro.common.other.anno.ManageApiAuth;
import com.hai.micro.common.other.anno.OpenApiAuth;
import com.hai.micro.common.other.anno.WapMiniApiAuth;
import com.hai.micro.common.other.constant.JwtAuthContext;
import com.hai.micro.common.other.constant.TenantCachePrefix;
import com.hai.micro.common.other.constant.UserAuthContext;
import com.hai.micro.common.other.enums.PlatformAuthTypeEnum;
import com.hai.micro.common.other.error.BusinessException;
import com.hai.micro.common.other.service.RedisService;
import com.hai.micro.common.other.vo.JwtAccessTokenVO;
import com.hai.micro.common.other.vo.UserAuthVO;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ClientAuthService
 * @Description 客户端调用TOKEN校验
 * @Author ZXH
 * @Date 2022/5/24 9:39
 * @Version 1.0
 **/
@Slf4j
@Service
public class ClientAuthService {

    private static final String CHAR_COLON = ":";

    @Autowired
    private RedisService redisService;

    public void handler(Object handler, JwtAccessTokenVO jwtAccessTokenVO) {
        PlatformAuthTypeEnum platformAuthTypeEnum = PlatformAuthTypeEnum.valueOf(jwtAccessTokenVO.getPlatformAuthType());
        // 校验平台权限注解 解决token跨平台使用的问题
        checkClientAuthType(handler, platformAuthTypeEnum);
        switch (platformAuthTypeEnum) {
            case Manage:
                handleManageUserAuth(jwtAccessTokenVO);
                break;
            case Machine:
                handleMachineAuth(jwtAccessTokenVO);
                break;
            case OpenAPI:
                handleOpenApiAuth(jwtAccessTokenVO);
                break;
            case WAP_MINI_APP:
                handleWapMiniAuth(jwtAccessTokenVO);
                break;
            default:
                throw new BusinessException("抱歉暂无访问权限，不支持的平台类型");
        }
    }

    /**
     * 校验客户端授权
     * 解决服务端的TOKEN不可以访问设备端
     *
     * @param handler
     * @param platformAuthTypeEnum
     */
    private void checkClientAuthType(Object handler, PlatformAuthTypeEnum platformAuthTypeEnum) {
        switch (platformAuthTypeEnum) {
            case Manage:
                boolean manageApiAuth = ((HandlerMethod) handler).hasMethodAnnotation(ManageApiAuth.class);
                if (!manageApiAuth) {
                    throw new BusinessException("抱歉暂无访问权限");
                }
            case Machine:
                boolean machineApiAuth = ((HandlerMethod) handler).hasMethodAnnotation(MachineApiAuth.class);
                if (!machineApiAuth) {
                    throw new BusinessException("抱歉暂无访问权限");
                }
            case OpenAPI:
                boolean openApiAuth = ((HandlerMethod) handler).hasMethodAnnotation(OpenApiAuth.class);
                if (!openApiAuth) {
                    throw new BusinessException("抱歉暂无访问权限");
                }
            case WAP_MINI_APP:
                boolean wapMiniApiAuth = ((HandlerMethod) handler).hasMethodAnnotation(WapMiniApiAuth.class);
                if (!wapMiniApiAuth) {
                    throw new BusinessException("抱歉暂无访问权限");
                }
            default:
                throw new BusinessException("抱歉暂无访问权限");
        }
    }

    /**
     * 处理管理端用户权限
     *
     * @param jwtAccessTokenVO
     */
    private void handleManageUserAuth(JwtAccessTokenVO jwtAccessTokenVO) {
        JwtAuthContext.setClientId(jwtAccessTokenVO.getClientId());
        JwtAuthContext.setTenantId(jwtAccessTokenVO.getTenantId());
        JwtAuthContext.setJwtAppId(jwtAccessTokenVO.getJwtAppId());
        UserAuthVO clientManageAuth = getClientManageAuth(jwtAccessTokenVO);
        if (clientManageAuth != null) {
            JwtAuthContext.setClientName(jwtAccessTokenVO.getClientName());
            UserAuthContext.setUserAuth(clientManageAuth);
        }
    }

    /**
     * 获取管理端用户权限
     *
     * @param jwtAccessTokenVO
     * @return
     */
    public UserAuthVO getClientManageAuth(JwtAccessTokenVO jwtAccessTokenVO) {
        String cacheAccessToken = null;
        if (Objects.nonNull(jwtAccessTokenVO.getClientId()) && Objects.nonNull(jwtAccessTokenVO.getJwtAppId())) {
            String key = new StringBuilder(TenantCachePrefix.USER_JWT_AUTH_INFO).append(jwtAccessTokenVO.getJwtAppId()).append(CHAR_COLON).append(jwtAccessTokenVO.getClientId()).toString();
            cacheAccessToken = redisService.getString(key);
        }
        if (cacheAccessToken == null) {
            throw new BusinessException("获取授权信息失败");
        }
        return JSONObject.parseObject(cacheAccessToken, UserAuthVO.class);
    }

    /**
     * 处理机器端权限
     *
     * @param jwtAccessTokenVO
     */
    private void handleMachineAuth(JwtAccessTokenVO jwtAccessTokenVO) {
        JwtAuthContext.setClientId(jwtAccessTokenVO.getClientId());
        JwtAuthContext.setTenantId(jwtAccessTokenVO.getTenantId());
        JwtAuthContext.setJwtAppId(jwtAccessTokenVO.getJwtAppId());
        UserAuthContext.machineAuth.set(jwtAccessTokenVO.getDeviceAuthVO());
    }

    /**
     * 处理外部平台权限
     *
     * @param jwtAccessTokenVO
     */
    private void handleOpenApiAuth(JwtAccessTokenVO jwtAccessTokenVO) {
        JwtAuthContext.setClientId(jwtAccessTokenVO.getClientId());
        JwtAuthContext.setTenantId(jwtAccessTokenVO.getTenantId());
        JwtAuthContext.setJwtAppId(jwtAccessTokenVO.getJwtAppId());
        UserAuthContext.openAuth.set(jwtAccessTokenVO.getOpenAuthVO());
    }

    /**
     * 处理网页小程序权限
     *
     * @param jwtAccessTokenVO
     */
    private void handleWapMiniAuth(JwtAccessTokenVO jwtAccessTokenVO) {
        JwtAuthContext.setClientId(jwtAccessTokenVO.getClientId());
        JwtAuthContext.setTenantId(jwtAccessTokenVO.getTenantId());
        JwtAuthContext.setJwtAppId(jwtAccessTokenVO.getJwtAppId());
        UserAuthContext.wapAuth.set(jwtAccessTokenVO.getWapAuthVO());
    }
}
