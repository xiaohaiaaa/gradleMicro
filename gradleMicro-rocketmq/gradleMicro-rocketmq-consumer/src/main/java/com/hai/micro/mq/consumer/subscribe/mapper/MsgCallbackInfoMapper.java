package com.hai.micro.mq.consumer.subscribe.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.hai.micro.mq.consumer.entity.MsgCallbackInfo;

/**
 * <p>
 * 消息回调信息 Mapper 接口
 * </p>
 *
 * @author zxh
 * @since 2022-08-12
 */
public interface MsgCallbackInfoMapper extends BaseMapper<MsgCallbackInfo> {

    /**
     * 获取所有有效配置
     * 
     * @return {@link MsgCallbackInfo}
     */
    @Select("select * from msg_callback_info where status = 1")
    List<MsgCallbackInfo> listValid();
}
