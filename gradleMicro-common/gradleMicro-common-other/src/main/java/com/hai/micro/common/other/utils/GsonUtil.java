package com.hai.micro.common.other.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

/**
 * @ClassName GsonUtil
 * @Description
 * @Author ZXH
 * @Date 2021/12/9 11:43
 * @Version 1.0
 **/
public class GsonUtil {

    private static class GsonHolder {
        // 注册Double转成Long解析器
        private static final Gson INSTANCE = new GsonBuilder()
                .registerTypeAdapter(Double.class, (JsonSerializer<Double>) (src, typeOfSrc, context) -> {
                    if (src == src.longValue()) {
                        return new JsonPrimitive(src.longValue());
                    }
                    return new JsonPrimitive(src);
                })
                // 禁止转义html标签
                .disableHtmlEscaping().create();
    }

    /**
     * 获取Gson实例，由于Gson是线程安全的，这里共同使用同一个Gson实例
     */
    public static Gson getGsonInstance() {
        return GsonHolder.INSTANCE;
    }
}
