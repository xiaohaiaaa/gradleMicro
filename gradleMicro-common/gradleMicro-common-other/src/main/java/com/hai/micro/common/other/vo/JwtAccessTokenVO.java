package com.hai.micro.common.other.vo;

import java.io.Serializable;

import com.hai.micro.common.other.enums.JwtAppIdEnum;
import com.hai.micro.common.other.enums.PlatformAuthTypeEnum;
import com.hai.micro.common.other.enums.SystemAuthTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName JwtAccessTokenVO
 * @Description 请求TOKEN请求对象
 * @Author ZXH
 * @Date 2022/5/23 11:51
 * @Version 1.0
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtAccessTokenVO implements Serializable {

    private static final long serialVersionUID = -6781178976152349635L;

    private String clientId;
    private String clientName;
    private String tenantId;
    private Long expiresIn;

    /**
     * 系统授权类型
     * @see SystemAuthTypeEnum
     */
    private String serviceAuthType;

    /**
     * 平台授权类型
     * @see PlatformAuthTypeEnum
     */
    private String platformAuthType;

    /**
     * 应用JWT类型
     * @see JwtAppIdEnum
     */
    private String jwtAppId;

    /**
     * 设备授权信息
     */
    private MachineAuthVO deviceAuthVO;
    /**
     * 开发平台授权信息
     */
    private OpenAuthVO openAuthVO;
    /**
     * 网页应用授权信息
     */
    private WapAuthVO wapAuthVO;

}
