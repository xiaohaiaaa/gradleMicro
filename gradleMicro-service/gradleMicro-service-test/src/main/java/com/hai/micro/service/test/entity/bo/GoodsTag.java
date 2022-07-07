package com.hai.micro.service.test.entity.bo;

import lombok.Data;

/**
 * @ClassName GoodsTag
 * @Description 商品标签
 * @Author ZXH
 * @Date 2021/12/20 13:41
 * @Version 1.0
 **/
@Data
public class GoodsTag {

    /**
     * 标签ID
     */
    private String tagId;

    /**
     * 标签名称
     */
    private String tagName;
}
