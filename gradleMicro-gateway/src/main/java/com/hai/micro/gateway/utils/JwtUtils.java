package com.hai.micro.gateway.utils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.hai.micro.common.other.enums.SystemAuthTypeEnum;
import com.hai.micro.common.other.error.BaseException;
import com.hai.micro.common.other.vo.JwtAccessTokenVO;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdUtil;
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
    public static final String SMS_KEY_SECRET = "zxh:sm4";
    private static SymmetricCrypto sm4;
    private static Long DEFAULT_EXPIRE_SECONDS;

    static {
        sm4 = SmUtil.sm4(SMS_KEY_SECRET.getBytes(StandardCharsets.UTF_8));
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
        String token = JWTUtil.createToken(payload, SMS_KEY_SECRET.getBytes(StandardCharsets.UTF_8));
        return sm4.encryptBase64(token, CharsetUtil.CHARSET_UTF_8);
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
            JWT jwt = JWTUtil.parseToken(sm4.decryptStr(token));
            if (jwt.verify()) {
                throw new BaseException("抱歉暂无访问权限");
            }
            if (jwt.validate(30L)) {
                throw new BaseException("登陆已过期请重新登陆");
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
}
