package com.hai.micro.gateway.filter;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import com.hai.micro.common.other.constant.AuthConstants;
import com.hai.micro.common.other.utils.WebUtils;
import com.hai.micro.common.other.vo.JwtAccessTokenVO;
import com.hai.micro.gateway.nacos.PropAutoRefresh;
import com.hai.micro.common.other.utils.JwtUtils;
import com.hai.micro.gateway.utils.ResponseUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @ClassName TokenAuthGlobalFilter
 * @Description 网关Token权限校验器
 * @Author ZXH
 * @Date 2022/6/2 14:19
 * @Version 1.0
 **/
@Slf4j
@Configuration(proxyBeanMethods = false)
public class TokenAuthGlobalFilter implements GlobalFilter, Ordered {

    /**
     * 项目白名单，忽略授权的接口地址，异步回调、注册等
     */
    private static Set<String> IGNORE_URL = Stream.of("/goods/recognition/notify/ks/v1", "/goods/modeling/notify/ks/v1").collect(Collectors.toSet());
    /**
     * 项目需要忽略授权的接口地址前缀 例如监控服务
     */
    private static Set<String> IGNORE_URL_PREFIX = Stream.of("/druid", "/actuator").collect(Collectors.toSet());

    @Autowired
    private PropAutoRefresh propAutoRefresh;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!propAutoRefresh.getTokenAuthSwitch()) {
            chain.filter(exchange);
        }
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String requestUrl = request.getURI().getRawPath();
        if (ignoreUrl(requestUrl)) {
            chain.filter(exchange);
        }
        String headerToken = exchange.getRequest().getHeaders().getFirst(AuthConstants.JWT_ACCESS_TOKEN);
        if (Strings.isBlank(headerToken)) {
            log.info("token为空，请求地址: {}", WebUtils.getIpReactive(request));
            return ResponseUtil.webFluxResponseWriter(response, "token为空，请重新登陆", null);
        }
        JwtAccessTokenVO jwtAccessTokenVO = JwtUtils.parseClientToken(headerToken);
        if (Objects.isNull(jwtAccessTokenVO)) {
            log.info("token解析失败，请求地址: {}", WebUtils.getIpReactive(request));
            return ResponseUtil.webFluxResponseWriter(response, "token解析失败，请重新登陆", null);
        }
        exchange.getAttributes().put("JwtAccessTokenVO", jwtAccessTokenVO);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 请求Url是否忽略校验
     *
     * @param requestUrl
     * @return
     */
    private Boolean ignoreUrl(String requestUrl) {
        if (propAutoRefresh.getWhiteUrlList().contains(requestUrl)) {
            return true;
        }
        if (IGNORE_URL.contains(requestUrl)) {
            return true;
        }
        for (String ignoreUrlPrefix : IGNORE_URL_PREFIX) {
            if (requestUrl.startsWith(ignoreUrlPrefix)) {
                return true;
            }
        }
        return false;
    }
}
