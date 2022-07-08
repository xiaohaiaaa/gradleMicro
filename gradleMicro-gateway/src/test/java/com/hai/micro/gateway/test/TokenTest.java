package com.hai.micro.gateway.test;

import org.junit.Test;

import com.hai.micro.common.other.enums.PlatformAuthTypeEnum;
import com.hai.micro.common.other.vo.JwtAccessTokenVO;
import com.hai.micro.common.other.utils.JwtUtils;

/**
 * @ClassName TokenTest
 * @Description
 * @Author ZXH
 * @Date 2022/7/8 13:50
 * @Version 1.0
 **/
public class TokenTest {

    @Test
    public void test() {
        JwtAccessTokenVO jwtAccessTokenVO = new JwtAccessTokenVO();
        jwtAccessTokenVO.setPlatformAuthType(PlatformAuthTypeEnum.Manage.name());
        jwtAccessTokenVO.setJwtAppId("1");
        jwtAccessTokenVO.setClientId("2");
        jwtAccessTokenVO.setClientName("zxh");
        jwtAccessTokenVO.setTenantId("3");
        System.out.println(JwtUtils.createClientToken(jwtAccessTokenVO));
    }
}
