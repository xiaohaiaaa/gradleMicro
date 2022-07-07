package com.hai.micro.common.other.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName OpenAuthVO
 * @Description 对外开放平台授权信息
 * @Author ZXH
 * @Date 2022/5/23 17:09
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpenAuthVO implements Serializable {

    private static final long serialVersionUID = 5490056496883713496L;

    /**
     * 门锁租户ID
     */
    private String dTenantId;

    /**
     * 商品识别租户ID
     */
    private String gTenantId;

}
