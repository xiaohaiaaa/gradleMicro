package com.hai.micro.service.test.firstTest.service.Impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hai.micro.common.other.entity.City;
import com.hai.micro.common.other.entity.OnlineTransLog;
import com.hai.micro.service.test.entity.vo.ImportVO;
import com.hai.micro.service.test.firstTest.mapper.CityMapper;
import com.hai.micro.service.test.firstTest.service.OnlineTransLogService;
import com.hai.micro.service.test.firstTest.service.TestService;
import com.hai.micro.service.test.listener.TestEventBO;
import com.hai.micro.service.test.utils.ThreadPoolUtil;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 13352
 */
@Service
@Slf4j
public class TestServiceImpl implements TestService {

    @Resource(name = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor executor;

    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private OnlineTransLogService onlineTransLogService;

    //男性和女性
    private final String GENDER_MALE = "MALE";
    private final String GENDER_FEMALE = "FEMALE";

    /**
     * 测试线程池
     */
    @Override
    public void testService() {
        int s = 10;
        //CountDownLatch countDownLatch = new CountDownLatch(s);
        int i = 1;
        while (i <= 10) {
            int j = i;

            //方式一
            /*executor.execute(() -> {
                //log.error(executor.getThreadNamePrefix() + j);
                log.error(Thread.currentThread().getName());
                //countDownLatch.countDown();
            });*/

            //方式二
            CompletableFuture<Integer> result = CompletableFuture.supplyAsync(() -> {
                log.info(Thread.currentThread().getName());
                return j;
            }, ThreadPoolUtil.THREAD_POOL_EXECUTOR);

            i++;
        }
        /*try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    @Cacheable(value = "UserCache", condition = "#id >= 3", key = "#id")
    @Override
    public City testEhcache(Long id) {
        City city = cityMapper.selectByPrimaryKey(id);
        return city;
    }

    @CacheEvict(value = "UserCache", key = "#id")
    @Override
    public String clearEhcache(Long id) {
        return "清理缓存" + id + "成功";
    }

    AtomicInteger number = new AtomicInteger(1);

    @Override
    public void testExecutor() {
        ThreadPoolUtil.execute(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("线程"+threadName+"开启！");
            try {
                Thread.sleep(10000);
                City city = cityMapper.selectByPrimaryKey(1L);
                city.setId(null);
                city.setName(String.valueOf(number.getAndIncrement()));
                cityMapper.insert(city);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程"+threadName+"执行结束!");
        });
    }

    @Override
    public List<ImportVO> testImportExcel(MultipartFile file, Integer ignoreRow) {
        List<ImportVO> importVOList = new ArrayList<>();
        /*InputStream inputStream = file.getInputStream();
        List<String[]> dataList = PoiUtil.readExcel(inputStream, ignoreRow);
        for (String[] str : dataList) {
            ImportVO importVO = new ImportVO();
            if (str.length >= 1) {
                importVO.setData1(str[0]);
            }
            if (str.length >= 2) {
                importVO.setData2(str[1]);
            }
            if (str.length >= 3) {
                importVO.setData3(str[2]);
            }
            if (str.length >= 4) {
                importVO.setData4(str[3]);
            }
            if (str.length >= 5) {
                importVO.setData5(str[4]);
            }
            importVOList.add(importVO);
        }*/
        ExcelReader reader = null;
        try {
            reader = ExcelUtil.getReader(file.getInputStream());
            List<List<Object>> read = reader.read(2);
            //List<Object> read = reader.readRow(3);
            System.out.println(read);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return importVOList;
    }

    @Override
    public void testExportExcel(HttpServletResponse response) {
        ExcelWriter excelWriter = null;
        ServletOutputStream outputStream = null;
        try {
            response.setContentType("application/vnd.ms-excel;charset=gbk");
            String headValue = String.format("attachment;filename=\"%s\"",
                    new String("城市列表.xls".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, headValue);
            // 写出xls
            excelWriter = ExcelUtil.getWriter();
            // 命名当前页
            excelWriter.renameSheet("城市列表");
            // 第一行标题
            List<String> headList = Arrays.asList("ID", "名称", "国家", "地区", "人口");
            excelWriter.writeHeadRow(headList);
            // 自定义表格标题别名
            excelWriter.addHeaderAlias("id", "ID");
            excelWriter.addHeaderAlias("name", "名称");
            excelWriter.addHeaderAlias("countryCode", "国家");
            excelWriter.addHeaderAlias("district", "地区");
            excelWriter.addHeaderAlias("population", "人口");
            // 查询输出数据
            List<City> cityList = new ArrayList<>();
            City city = cityMapper.selectById("1");
            cityList.add(city);
            // 写出
            excelWriter.write(cityList, false);
            outputStream = response.getOutputStream();
            excelWriter.flush(outputStream, true);
        } catch (Exception e) {
            log.error("export record error", e);
        } finally {
            // 关闭writer，释放内存
            if (Objects.nonNull(excelWriter)) {
                excelWriter.close();
            }
            // 此次记得关闭输出servlet流
            IoUtil.close(outputStream);
        }
    }

    @Override
    public void testSelectUpdate1() {
        City city = cityMapper.selectById("1");
        System.out.println("before sleep: " + city);
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        city.setCountryCode("003");
        cityMapper.updateById(city);
    }

    @Override
    public void testSelectUpdate2() {
        City city = cityMapper.selectForUpdate(1l);
        city.setCountryCode("002");
        cityMapper.updateById(city);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void testEvent() {
        City city = new City();
        city.setId(2);
        city.setName("广州市");
        city.setCountryCode("003");
        city.setDistrict("广东");
        city.setPopulation(1);
        cityMapper.insert(city);
        eventPublisher.publishEvent(new TestEventBO(this, city));
    }

    @Override
    public Map testSelectSharding() {
        Map map = new HashMap<>();
        City city = cityMapper.selectById(1l);
        OnlineTransLog logByTradeNo = onlineTransLogService.getLogByTradeNo("1", "0162");
        map.put("City", city);
        map.put("OnlineTransLog", logByTradeNo);
        return map;
    }
}
