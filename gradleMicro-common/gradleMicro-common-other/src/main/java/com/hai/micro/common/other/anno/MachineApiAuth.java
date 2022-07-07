package com.hai.micro.common.other.anno;

import java.lang.annotation.*;

/**
 * 机器端权限校验注解
 * @author zxh
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MachineApiAuth {
}
