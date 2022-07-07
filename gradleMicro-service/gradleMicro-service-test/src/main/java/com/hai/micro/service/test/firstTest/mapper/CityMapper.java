package com.hai.micro.service.test.firstTest.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hai.micro.service.test.entity.City;

/**
 * @Entity com.hai.test.domain.City
 */
@Mapper
public interface CityMapper extends BaseMapper<City> {

    int insert(City record);

    City selectByPrimaryKey(Long id);

    City selectForUpdate(@Param("id") Long id);

}
