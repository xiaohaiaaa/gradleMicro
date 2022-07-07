package com.hai.micro.common.other.error;

/**
 * @ClassName BusinessException
 * @Description 自定义业务异常类
 * @Author ZXH
 * @Date 2021/12/9 10:45
 * @Version 1.0
 **/
public class BusinessException extends BaseException {

    private static final long serialVersionUID = -433474717355185709L;

    public BusinessException(IErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }
}
