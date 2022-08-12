package com.hai.micro.common.event.bo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @ClassName BasePushBo
 * @Description: 事件消息基类
 * @Author zxh
 * @Date 2022/08/12 14:40
 * @Version 1.0
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BasePushBo<T> implements Serializable {

    private static final long serialVersionUID = 1469814315926944358L;

    /**
     * 事件消息id
     */
    private String msgId;

    /**
     * 消息类型
     */
    private BasePushType type;

    /**
     * mq消息topic
     */
    private BaseTopicEnum topic;

    /**
     * 具体业务分发tag
     */
    private BaseTagEnum tag;

    /**
     * 具体发送内容参数对象
     */
    private T content;

}
