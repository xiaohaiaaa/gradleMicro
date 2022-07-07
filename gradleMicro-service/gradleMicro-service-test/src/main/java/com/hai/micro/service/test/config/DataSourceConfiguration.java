package com.hai.micro.service.test.config;

import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.provider.AbstractDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;

import cn.hutool.core.bean.BeanUtil;

/**
 * @ClassName DataSourceConfiguration
 * @Description 动态数据源配置;使用@DS注解切换数据源
 * @Author ZXH
 * @Date 2022/5/16 15:56
 * @Version 1.0
 **/
@Configuration
@AutoConfigureBefore({DynamicDataSourceAutoConfiguration.class, SpringBootConfiguration.class})
public class DataSourceConfiguration {

    /**
     * 分库分表数据源名称
     */
    private static final String SHARDING_DATA_SOURCE_NAME = "trade_sharding";

    /**
     * 动态数据源配置项
     */
    @Autowired
    private DynamicDataSourceProperties properties;

    /**
     * sharding数据源
     */
    @Lazy
    @Resource
    private ShardingDataSource shardingDataSource;

    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProvider() {
        Map<String, DataSourceProperty> dataSourcePropertyMap = properties.getDatasource();
        return new AbstractDataSourceProvider() {
            @Override
            public Map<String, DataSource> loadDataSources() {
                Map<String, DataSource> dataSourceMap = createDataSourceMap(dataSourcePropertyMap);
                // 将 sharding jdbc 管理的数据源也交给动态数据源管理
                dataSourceMap.put(SHARDING_DATA_SOURCE_NAME, shardingDataSource);
                return dataSourceMap;
            }
        };
    }

    /**
     * 注入sqlSessionFactory 和 sqlSessionTemplate，不加这个方法会报错找不到
     *
     * @return
     */
    @Primary
    @Bean
    public DataSource dataSource() {
        return BeanUtil.copyProperties(properties, DynamicRoutingDataSource.class);
    }
}
