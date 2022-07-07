package com.hai.micro.common.other.anno;

import java.lang.annotation.*;

/**
 * 对外开放接口权限校验
 * @author zxh
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpenApiAuth {
}
