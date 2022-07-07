package com.hai.micro.common.other.anno;

import java.lang.annotation.*;

/**
 * 管理端接口权限校验注解
 * @author zxh
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ManageApiAuth {
}
