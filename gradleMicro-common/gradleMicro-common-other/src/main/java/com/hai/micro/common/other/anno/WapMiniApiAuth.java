package com.hai.micro.common.other.anno;

import java.lang.annotation.*;

/**
 * h5网页和小程序权限校验注解
 * @author zxh
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WapMiniApiAuth {
}
