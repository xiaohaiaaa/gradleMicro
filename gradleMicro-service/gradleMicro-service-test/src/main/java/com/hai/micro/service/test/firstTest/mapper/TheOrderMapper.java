package com.hai.micro.service.test.firstTest.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hai.micro.common.other.entity.TheOrder;

public interface TheOrderMapper extends BaseMapper<TheOrder> {

    int batchInsert(@Param("list") List<TheOrder> list);
}