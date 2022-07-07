package com.hai.micro.service.test.entity.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @ClassName BasePushBO
 * @Description 推送消息泛型对象
 * @Author ZXH
 * @Date 2022/3/1 14:48
 * @Version 1.0
 **/
@Data
public class BasePushBO<T> implements Serializable {
    private static final long serialVersionUID = -6579001915537536780L;

    /**
     * 推送类型
     */
    private String type;

    /**
     * 推送内容
     */
    private T content;
}
