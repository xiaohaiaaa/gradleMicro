package com.hai.micro.common.other.enums;

import com.hai.micro.common.other.error.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName ResponseCode
 * @Description 请求相应枚举类
 * @Author ZXH
 * @Date 2022/6/2 16:11
 * @Version 1.0
 **/
public enum ResponseCode implements IErrorCode {
    SUCCESS(200, "操作成功"),
    FAIL(-1, "系统错误，请稍后重试");

    private int code;
    private String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
