package com.hai.micro.gateway.nacos;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @ClassName PropAutoRefresh
 * @Description Nacos动态刷新属性
 * @Author ZXH
 * @Date 2022/6/2 14:45
 * @Version 1.0
 **/
@RefreshScope
@Component
@Data
public class PropAutoRefresh {

    @Value("${token.auth.switch:true}")
    private Boolean tokenAuthSwitch;

    @Value("#{'${white.url.list:/error,/notify}'.split(',')}")
    private Set<String> whiteUrlList;
}
