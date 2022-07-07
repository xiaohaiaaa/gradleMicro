package com.hai.micro.service.test.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 商品信息表
 * @TableName stock
 */
@TableName(value ="stock")
@Data
public class Stock implements Serializable {
    /**
     * 编号
     */
    @TableId(type = IdType.AUTO)
    private Integer stockId;

    /**
     * 商品名称
     */
    private String stockName;

    /**
     * 商品库存
     */
    private Integer stockNumber;

    private static final long serialVersionUID = 1L;

}