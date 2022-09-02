package com.hai.micro.mq.consumer.subscribe.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.hai.micro.mq.consumer.entity.MsgSubscribeConfig;

/**
 * <p>
 * 消息关注配置 Mapper 接口
 * </p>
 *
 * @author liuawei
 * @since 2021-12-16
 */
public interface MsgSubscribeConfigMapper extends BaseMapper<MsgSubscribeConfig> {

    /**
     * 获取有效的配置
     * 
     * @return {@link MsgSubscribeConfig}
     */
    @Select("select * from msg_subscribe_config where status = 1")
    List<MsgSubscribeConfig> listValid();

}
