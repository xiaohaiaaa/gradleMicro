package com.hai.micro.common.other.bo;

import lombok.Data;

/**
 * @ClassName MqMessageInfo
 * @Description: mq消息封装对象
 * @Author zxh
 * @Date 2022/08/12 10:36
 * @Version 1.0
 **/
@Data
public class MqMessageInfo {

    private String msgId;

    /**
     * MQ topic
     * required:true
     */
    private String topic;
    /**
     * MQ tag
     * required:true
     */
    private String tag;

    /**
     * 消息体内容
     */
    private String content;

}
