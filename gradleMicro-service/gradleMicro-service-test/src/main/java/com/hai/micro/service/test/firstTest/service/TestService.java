package com.hai.micro.service.test.firstTest.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.hai.micro.common.other.entity.City;
import com.hai.micro.service.test.entity.vo.ImportVO;

/**
 * @author 13352
 */
public interface TestService {
    /**
     * 测试多线程
     */
    void testService();

    /**
     * 测试mq异步消息
     * @param id
     * @return
     */
    City testRocketMq(Long id);

    /**
     * 测试Ehcache缓存加载
     * @param id
     * @return
     */
    City testEhcache(Long id);

    /**
     * 测试Ehcache缓存清理
     * @param id
     * @return
     */
    String clearEhcache(Long id);

    /**
     * 测试多线程
     */
    void testExecutor();

    /**
     * 测试表格导入
     *
     * @param file
     * @param ignoreRow
     * @return
     */
    List<ImportVO> testImportExcel(MultipartFile file, Integer ignoreRow);

    /**
     * 测试表格导出
     *
     * @param response
     */
    void testExportExcel(HttpServletResponse response);

    /**
     * 测试表级锁
     */
    void testSelectUpdate1();
    void testSelectUpdate2();

    /**
     * 测试事件监听和发布
     */
    void testEvent();

    /**
     * 测试sharding集成多数据源
     * @return
     */
    Map testSelectSharding();

}
