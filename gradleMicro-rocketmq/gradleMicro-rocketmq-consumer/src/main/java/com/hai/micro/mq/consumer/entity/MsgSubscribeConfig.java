package com.hai.micro.mq.consumer.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 消息关注配置
 * </p>
 *
 * @author zxh
 * @since 2022-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MsgSubscribeConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 一级消息类型
     */
    private String topicName;

    /**
     * 二级消息类型("*"星号表示所有;"|"竖划线分隔)
     */
    private String tagName;

    /**
     * 状态1正常;0禁用
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModified;


}
