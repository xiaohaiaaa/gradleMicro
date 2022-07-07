package com.hai.micro.service.test.nacos;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @ClassName PropAutoRefresh
 * @Description 动态刷新配置获取
 * @Author ZXH
 * @Date 2021/12/1 16:15
 * @Version 1.0
 **/
@Component
@RefreshScope
@Data
public class PropAutoRefresh {

}
