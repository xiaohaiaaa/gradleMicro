package com.hai.micro.service.test.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

/**
 * 订单信息表
 */
@Data
public class TheOrder {
    /**
     * 订单编号
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String orderId;

    /**
     * 订单名称
     */
    private String orderName;

    /**
     * 订单用户
     */
    private String orderUser;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime modifyTime;

    /**
     * 创建用户
     */
    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    /**
     * 更新用户
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String modifyUser;
}