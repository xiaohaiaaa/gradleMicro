package com.hai.micro.common.other.anno;

import java.lang.annotation.*;

/**
 * @author 13352
 * @description 切面层定义
 * @date 2021.07.20 22:41
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Section {
    String gender();
    int age();
}
