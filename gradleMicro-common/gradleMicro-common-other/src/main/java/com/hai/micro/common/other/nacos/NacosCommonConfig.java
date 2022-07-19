package com.hai.micro.common.other.nacos;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @ClassName NacosCommonConfig
 * @Description
 * @Author ZXH
 * @Date 2021/12/1 16:15
 * @Version 1.0
 **/
@Component
@RefreshScope
@Data
public class NacosCommonConfig {

    /**
     * 服务名称
     */
    @Value("${spring.application.name:''}")
    private String applicationName;

    /**
     * 微服务认证开关
     */
    @Value("${micro.service.access.enable:true}")
    private Boolean serviceAccessEnable;

    /**
     * 微服务认证密钥集合
     */
    @Value("#{'${micro.service.access.secrets:SJ7zE8E1zv3UmjKU,E4+E3GiHcEuXbLpg}'.split(',')}")
    private List<String> serviceAccessSecrets;

    /**
     * 商户新增套餐参数
     */
    @Value("${mch.template:{\"templateName\":\"默认商品套餐\",\"packageType\":\"0\",\"defaultValue\":\"0\",\n"
            + "\"deviceType\":\"1,2\"}}")
    private String mchTemplate;

    /**
     * 商户标签
     */
    @Value("${goods.tag:[{\"tagId\":\"1234\",\"tagName\":\"标签一\"},{\"tagId\":\"5678\",\"tagName\":\"标签二\"}]}")
    private String goodsTag;

    /**
     * 线上运维请求头
     */
    @Value("${dev.ops.req.header:Hai-Dev-Ops}")
    private String devOpsReqHeader;

    /**
     * 签名校验开关
     *
     */
    @Value("${param.sign.check.mode:close}")
    private String paramSignCheckMode;

    /**
     * 签名校验有效期，30秒
     */
    @Value("${param.sign.timestamp:60000}")
    private Integer paramSignTimeStamp;

    /**
     * jwtAppId 枚举
     */
    @Value("${app.jwtId:{\"PLATFORM_MANAGE\":\"loaSrYmlhlXevWlI0\",\"MCH_MANAGE\":\"loaSrYmlhlXevWlI1\",\"ANDROID\":\"loaSrYmlhlXevWlI2\",\n"
            + "\"IOT\":\"loaSrYmlhlXevWlI3\",\"FACTORY_MANAGE\":\"loaSrYmlhlXevWlI4\",\"WAP\":\"loaSrYmlhlXevWlI5\"}}")
    private String appJwtId;

    /**
     * AI识别成功，是否再转客服复核
     */
    @Value("${user.review:false}")
    private Boolean userReview;

    /**
     * 白名单地址
     */
    @Value("#{'${web.interceptor.whitelist:/error,/favicon.ico}'.split(',')}")
    private Set<String> whiteListStrs;
}
