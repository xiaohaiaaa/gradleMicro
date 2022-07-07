package com.hai.micro.common.other.vo;

import java.io.Serializable;
import java.util.Set;

import lombok.Data;

/**
 * @ClassName UserAuthVO
 * @Description 用户授权信息
 * @Author ZXH
 * @Date 2022/5/17 16:23
 * @Version 1.0
 **/
@Data
public class UserAuthVO implements Serializable {

    private static final long serialVersionUID = -6451715167519513464L;

    private String userId;
    private Integer userType;
    private String userName;
    private String tenantId;
    private String jwtAppId;
    private Set<String> roleSet;
    private Set<String> ruleSet;
    private String assessToken;
    private Integer isAdmin;
}
