package com.hai.micro.common.other.error;

/**
 * @ClassName BaseErrorCode
 * @Description 错误码信息定义
 * @Author ZXH
 * @Date 2021/12/9 10:36
 * @Version 1.0
 **/
public enum BaseErrorCode implements IErrorCode{

    SIGN_EMPTY_ERROR(1000, "签名验证为空"),
    SIGN_OVERDUE_ERROR(1001, "签名验证过期"),
    SIGN_CHECK_ERROR(1002, "签名验证失败"),
    SIGN_CHECK_FAIL(1002, "签名验证错误，抱歉暂无访问权限");

    private int code;
    private String message;

    BaseErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
