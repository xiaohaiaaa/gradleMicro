package com.hai.micro.service.test.entity.vo;

import lombok.Data;

/**
 * @ClassName ImportVO
 * @Description
 * @Author ZXH
 * @Date 2022/2/23 10:42
 * @Version 1.0
 **/
@Data
public class ImportVO {

    /**
     * 纷享客户名称
     */
    private String tenantName;

    /**
     * 纷享客户编号
     */
    private String tenantId;

    /**
     * 客户类型
     */
    private String tenantType;

    /**
     * 所在省
     */
    private String province;

    /**
     * 所在市
     */
    private String city;

    /**
     * 业务员
     */
    private String salesman;
}
