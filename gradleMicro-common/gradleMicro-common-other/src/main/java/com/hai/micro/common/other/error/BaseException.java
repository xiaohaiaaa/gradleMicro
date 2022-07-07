package com.hai.micro.common.other.error;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName BaseExection
 * @Description 自定义异常类
 * @Author ZXH
 * @Date 2021/12/9 10:28
 * @Version 1.0
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseException extends RuntimeException{

    private static final long serialVersionUID = -2841977170163709634L;

    private String errorMessage;
    private IErrorCode errorCode;

    public BaseException(String errorMessage, IErrorCode errorCode) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public BaseException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
