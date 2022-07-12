package com.hai.micro.gateway.gray;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSONObject;
import com.hai.micro.common.other.constant.BizConstants;
import com.hai.micro.common.other.enums.JwtAppIdEnum;
import com.hai.micro.common.other.vo.JwtAccessTokenVO;
import com.hai.micro.gateway.constant.GrayHeaderRequestConstant;

/**
 * @ClassName GrayRuleHandler
 * @Description 灰度规则处理
 * @Author ZXH
 * @Date 2022/5/31 10:46
 * @Version 1.0
 **/
@RefreshScope
@Component
public class GrayRuleHandler {

    /**
     * 灰度的门锁租户IDs
     */
    @Value("#{'${gray.dTenantIds:}'.split(',')}")
    private List<String> grayDoorTenantIds;
    /**
     * 灰度的商品识别租户IDs
     */
    @Value("#{'${gary.gTenantIds:}'.split(',')}")
    private List<String> grayGoodsTenantIds;
    /**
     * 灰度的租户用户IDS
     */
    @Value("#{'${gary.tenant.userIds:}'.split(',')}")
    private List<String> grayTenantUserIds;
    /**
     * 灰度的开放平台应用ID
     */
    @Value("#{'${gary.appIds:}'.split(',')}")
    private List<String> grayAppIds;
    /**
     * 灰度的机器IDS
     */
    @Value("#{'${gary.machineIds:}'.split(',')}")
    private List<String> grayMachineIds;
    /**
     * 灰度的机器版本
     */
    @Value("#{'${gary.androidVersions:}'.split(',')}")
    private List<String> grayAndroidVersions;

    public JSONObject handle(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        JSONObject result = null;
        // 基于前端请求头判断是否需要灰度
        for (String headKey : headers.keySet()) {
            String headValue = headers.getFirst(headKey);
            boolean isGray = (GrayHeaderRequestConstant.GRAY_HEADER_FLAG.equals(headKey) && BizConstants.COMMON_JUDGE_YES.equals(headValue))
                    || (GrayHeaderRequestConstant.GRAY_MACHINE_HEADER.equals(headKey) && grayMachineIds.contains(headValue))
                    || (GrayHeaderRequestConstant.GRAY_DOOR_TENANT_HEADER.equals(headKey) && grayDoorTenantIds.contains(headValue))
                    || (GrayHeaderRequestConstant.GRAY_GOODS_TENANT_HEADER.equals(headKey) && grayGoodsTenantIds.contains(headValue))
                    || (GrayHeaderRequestConstant.GRAY_OPEN_APP_HEADER.equals(headKey) && grayAppIds.contains(headValue))
                    || (GrayHeaderRequestConstant.GRAY_MACHINE_VERSION.equals(headKey) && grayAndroidVersions.contains(headValue))
                    || (GrayHeaderRequestConstant.GRAY_TENANT_USER_HEADER.equals(headKey) && grayTenantUserIds.contains(headValue));
            if (isGray) {
                result = new JSONObject();
                result.put(BizConstants.GATEWAY_GRAY, BizConstants.COMMON_JUDGE_YES);
                break;
            }
        }
        // 基于请求Token判断是否需要灰度
        Object jwtObject = exchange.getAttribute("JwtAccessTokenVO");
        if (jwtObject instanceof JwtAccessTokenVO) {
            JwtAccessTokenVO jwtAccessTokenVO = (JwtAccessTokenVO) jwtObject;
            String jwtAppId = jwtAccessTokenVO.getJwtAppId();
            if (JwtAppIdEnum.OPEN.name().equals(jwtAppId)) {
                if (grayAppIds.contains(jwtAccessTokenVO.getClientId())) {
                    result = new JSONObject();
                    result.put(BizConstants.GATEWAY_GRAY, BizConstants.COMMON_JUDGE_YES);
                    return result;
                }
            }
            if (JwtAppIdEnum.ANDROID.name().equals(jwtAppId)) {
                if (grayMachineIds.contains(jwtAccessTokenVO.getClientId())) {
                    result = new JSONObject();
                    result.put(BizConstants.GATEWAY_GRAY, BizConstants.COMMON_JUDGE_YES);
                    return result;
                }
            }
            if (Objects.nonNull(jwtAppId) && jwtAppId.startsWith(JwtAppIdEnum.MANAGE.name())) {
                if (grayGoodsTenantIds.contains(jwtAccessTokenVO.getTenantId())) {
                    result = new JSONObject();
                    result.put(BizConstants.GATEWAY_GRAY, BizConstants.COMMON_JUDGE_YES);
                    return result;
                }
                if (grayDoorTenantIds.contains(jwtAccessTokenVO.getTenantId())) {
                    result = new JSONObject();
                    result.put(BizConstants.GATEWAY_GRAY, BizConstants.COMMON_JUDGE_YES);
                    return result;
                }
                if (grayTenantUserIds.contains(jwtAccessTokenVO.getTenantId())) {
                    result = new JSONObject();
                    result.put(BizConstants.GATEWAY_GRAY, BizConstants.COMMON_JUDGE_YES);
                    return result;
                }
            }
        }
        return result;
    }
}
