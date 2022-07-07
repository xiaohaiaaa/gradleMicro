package com.hai.micro.common.other.constant;

/**
 * @ClassName JwtAuthContext
 * @Description Jwt授权参数容器
 * @Author ZXH
 * @Date 2022/5/24 10:31
 * @Version 1.0
 **/
public class JwtAuthContext {

    /**
     * 机构ID
     */
    public static ThreadLocal<String> TENANT_ID_LOCAL = new ThreadLocal();
    /**
     * jwt应用ID
     */
    public static ThreadLocal<String> JWT_APP_ID_LOCAL = new ThreadLocal();
    /**
     * 用户ID 终端ID
     */
    public static ThreadLocal<String> CLIENT_ID_LOCAL = new ThreadLocal();
    /**
     * 用户名称 终端编码
     */
    public static ThreadLocal<String> CLIENT_NAME_LOCAL = new ThreadLocal();

    public static String getClientName() {
        return CLIENT_NAME_LOCAL.get();
    }

    public static void setClientName(String clientName) {
        CLIENT_NAME_LOCAL.set(clientName);
    }

    public static String getJwtAppId() {
        return JWT_APP_ID_LOCAL.get();
    }

    public static void setJwtAppId(String jwtAppId) {
        JWT_APP_ID_LOCAL.set(jwtAppId);
    }

    public static String getClientId() {
        return CLIENT_ID_LOCAL.get();
    }

    public static void setClientId(String clientId) {
        CLIENT_ID_LOCAL.set(clientId);
    }

    public static String getTenantId() {
        return TENANT_ID_LOCAL.get();
    }

    public static void setTenantId(String tenantId) {
        TENANT_ID_LOCAL.set(tenantId);
    }

    public static void clear() {
        JWT_APP_ID_LOCAL.remove();
        CLIENT_ID_LOCAL.remove();
        TENANT_ID_LOCAL.remove();
        CLIENT_NAME_LOCAL.remove();
    }

}
