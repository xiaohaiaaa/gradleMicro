package com.hai.micro.common.other.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

/**
 * 城市资源表
 * @author zxh
 * @TableName city
 */
@Data
public class City implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 国家
     */
    private String countryCode;

    /**
     * 地区
     */
    private String district;

    /**
     * 人口
     */
    private Integer population;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;

}