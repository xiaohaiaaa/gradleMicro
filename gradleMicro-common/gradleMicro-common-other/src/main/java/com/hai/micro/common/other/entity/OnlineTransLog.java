package com.hai.micro.common.other.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

/**
 * @ClassName OnlineTransLog
 * @Description
 * @Author ZXH
 * @Date 2022/5/16 16:50
 * @Version 1.0
 **/
@Data
public class OnlineTransLog implements Serializable {
    private static final long serialVersionUID = -5069537256263287742L;

    /**
     * 自增主键
     */
    @TableId
    private String id;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 商户ID
     */
    private String mchId;

    /**
     * 商户账号
     */
    private String mchAccount;

    /**
     * 商户终端ID
     */
    private String mchTerminalId;

    /**
     * 支付类型
     */
    private String payType;

    /**
     * 用户标识（微信openId,支付宝账户名）
     */
    private String userId;

    /**
     * 用户账户
     */
    private String userAccount;

    /**
     * 设备信息 默认保存机器编码
     */
    private String machineCode;

    /**
     * 一级机构Id
     */
    private String rootOrgId;

    /**
     * 交易简介
     */
    private String subject;

    /**
     * 交易详情
     */
    private String body;

    /**
     * 商户订单号
     */
    private String tradeNo;

    /**
     * 支付服务商单号
     */
    private String outTradeNo;

    /**
     * 如果是付款方微信，支付宝（微信，支付宝的订单号）
     */
    private String transactionId;

    /**
     * 交易金额 分为单位
     */
    private Integer totalFee;

    /**
     * 支付金额 分为单位
     */
    private Integer payFee;

    /**
     * 优惠金额 分为单位
     */
    private Integer couponFee;

    /**
     * 货币类型
     */
    private String feeType;

    /**
     * 交易类型
     */
    private String tradeType;

    /**
     * 支付创建时间
     */
    private String orderCreate;

    /**
     * 支付完成时间
     */
    private String orderFinish;

    private Date gmtCreated;

    private Date gmtModified;
}
