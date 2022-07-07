package com.hai.micro.common.other.error;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName BusinessException
 * @Description 自定义业务异常类
 * @Author ZXH
 * @Date 2021/12/9 10:28
 * @Version 1.0
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException{

    private static final long serialVersionUID = -2841977170163709634L;

    private String errorMessage;
    private IErrorCode errorCode;

    public BusinessException(String errorMessage, IErrorCode errorCode) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public BusinessException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorMessage = errorCode.getMessage();
        this.errorCode = errorCode;
    }

    public BusinessException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
