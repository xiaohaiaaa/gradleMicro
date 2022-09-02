package com.hai.micro.common.other.utils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import com.hai.micro.common.other.enums.SystemAuthTypeEnum;
import com.hai.micro.common.other.error.BusinessException;
import com.hai.micro.common.other.vo.JwtAccessTokenVO;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.RegisteredPayload;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName JwtUtils
 * @Description Jwt工具类
 * @Author ZXH
 * @Date 2022/5/17 17:45
 * @Version 1.0
 **/
@Slf4j
public class JwtUtils {

    public static final String JWT_KEY_SECRET = "HmacSHA256:zxh";
    // 这里的密钥key值需要是128bit的
    public static final String SMS_KEY_SECRET = "cPVmnN2Ci5hvOrub";
    private static SymmetricCrypto sm4;
    private static Long DEFAULT_EXPIRE_SECONDS;

    static {
        sm4 = SmUtil.sm4(StrUtil.bytes(SMS_KEY_SECRET, StandardCharsets.UTF_8));
        DEFAULT_EXPIRE_SECONDS = 86400000L;
    }

    /**
     * 生成token
     *
     * @param jwtAccessTokenVO
     * @return
     */
    public static String createClientToken(JwtAccessTokenVO jwtAccessTokenVO) {
        Map<String, Object> payload = new HashMap();
        // 处理token过期
        payload.put(RegisteredPayload.ISSUED_AT, System.currentTimeMillis());
        payload.put(RegisteredPayload.EXPIRES_AT, jwtAccessTokenVO.getExpiresIn() == null ? DEFAULT_EXPIRE_SECONDS : jwtAccessTokenVO.getExpiresIn());
        payload.put(RegisteredPayload.JWT_ID, IdUtil.objectId());
        // 默认值，客户端访问的token，jwt签发者
        payload.put(RegisteredPayload.ISSUER, SystemAuthTypeEnum.CLIENT.name());
        // 接收jwt的一方
        payload.put(RegisteredPayload.AUDIENCE, jwtAccessTokenVO.getPlatformAuthType());
        // jwt所面向的用户
        payload.put(RegisteredPayload.SUBJECT, jwtAccessTokenVO.getJwtAppId());
        // 自定义附加信息
        payload.put("clientId", jwtAccessTokenVO.getClientId());
        payload.put("clientName", jwtAccessTokenVO.getClientName());
        payload.put("tenantId", jwtAccessTokenVO.getTenantId());
        if (jwtAccessTokenVO.getDeviceAuthVO() != null) {
            payload.put("deviceAuthVO", jwtAccessTokenVO.getDeviceAuthVO());
        }
        if (jwtAccessTokenVO.getOpenAuthVO() != null) {
            payload.put("openAuthVO", jwtAccessTokenVO.getOpenAuthVO());
        }
        String token = JWTUtil.createToken(payload, JWT_KEY_SECRET.getBytes(StandardCharsets.UTF_8));
        return sm4.encryptHex(token, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * token解析
     *
     * @param token
     * @return
     */
    public static JwtAccessTokenVO parseClientToken(String token) {
        JwtAccessTokenVO jwtAccessTokenVO = null;
        try {
            JWT jwt = JWTUtil.parseToken(sm4.decryptStr(token, CharsetUtil.CHARSET_UTF_8));
            if (jwt.verify()) {
                throw new BusinessException("抱歉暂无访问权限");
            }
            if (jwt.validate(30L)) {
                throw new BusinessException("登陆已过期请重新登陆");
            }
            JSONObject jsonObject = jwt.getPayloads();
            jwtAccessTokenVO = jsonObject.toBean(JwtAccessTokenVO.class);
            jwtAccessTokenVO.setExpiresIn(jsonObject.getLong(RegisteredPayload.EXPIRES_AT));
            jwtAccessTokenVO.setJwtAppId(jsonObject.getStr(RegisteredPayload.SUBJECT));
            jwtAccessTokenVO.setPlatformAuthType(jsonObject.getStr(RegisteredPayload.AUDIENCE));
            jwtAccessTokenVO.setServiceAuthType(jsonObject.getStr(RegisteredPayload.ISSUER));
        } catch (Exception e) {
            log.error("parseToken error", e);
        }
        return jwtAccessTokenVO;
    }

    /**
     * 生产回调token
     *
     * @param applicationName
     * @return
     */
    public static String createServiceToken(String applicationName) {
        Map<String, Object> payload = new HashMap<>();
        // 处理token过期
        payload.put(RegisteredPayload.ISSUED_AT, System.currentTimeMillis());
        Long expTime = LocalDateTime.now().plusYears(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        payload.put(RegisteredPayload.EXPIRES_AT,expTime);
        payload.put(RegisteredPayload.JWT_ID, IdUtil.objectId());
        // 默认值 客户端访问的token
        payload.put(RegisteredPayload.ISSUER, SystemAuthTypeEnum.SERVICE.name());
        // 接收jwt的一方
        payload.put(RegisteredPayload.SUBJECT, applicationName);
        String jwtToken = JWTUtil.createToken(payload, JWT_KEY_SECRET.getBytes(StandardCharsets.UTF_8));
        return sm4.encryptBase64(jwtToken, CharsetUtil.CHARSET_UTF_8);
    }
}
