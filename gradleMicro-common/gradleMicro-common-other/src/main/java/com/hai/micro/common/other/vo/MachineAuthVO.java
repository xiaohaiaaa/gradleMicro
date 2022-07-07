package com.hai.micro.common.other.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName MachineAuthVO
 * @Description 设备授权信息
 * @Author ZXH
 * @Date 2022/5/23 17:08
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MachineAuthVO implements Serializable {

    private static final long serialVersionUID = 7702963349999161958L;

    /**
     * 租户类型,1:门锁,2:视频识别
     */
    private Integer tenantType;

    /**
     * 出厂编号
     */
    private String machineCode;

}
