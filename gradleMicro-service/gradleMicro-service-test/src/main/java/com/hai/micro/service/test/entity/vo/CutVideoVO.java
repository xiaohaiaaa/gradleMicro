package com.hai.micro.service.test.entity.vo;

import lombok.Data;

/**
 * @ClassName CutVideoVO
 * @Description
 * @Author ZXH
 * @Date 2021/12/6 15:28
 * @Version 1.0
 **/
@Data
public class CutVideoVO {

    /**
     * 请求文件路径
     */
    String url;

    /**
     * 切割数量
     */
    Integer total;
}
