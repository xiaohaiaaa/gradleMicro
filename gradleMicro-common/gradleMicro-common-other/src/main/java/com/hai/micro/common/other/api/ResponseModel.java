package com.hai.micro.common.other.api;

import java.io.Serializable;

import com.hai.micro.common.other.enums.ResponseCode;
import com.hai.micro.common.other.error.IErrorCode;

import lombok.Data;

/**
 * @ClassName ResponseModel
 * @Description 统一请求相应包装实体类
 * @Author ZXH
 * @Date 2022/6/2 15:59
 * @Version 1.0
 **/
@Data
public class ResponseModel<T> implements Serializable {

    private static final long serialVersionUID = 160998010933387880L;

    private boolean isSuccess = false;
    private int code;
    private String message;
    private T content;

    public ResponseModel(int code, String message, T content) {
        this.code = code;
        this.message = message;
        this.content = content;
        if (code == ResponseCode.SUCCESS.getCode()) {
            isSuccess = true;
        }
    }

    public static <T> ResponseModel<T> success(T content) {
        return new ResponseModel<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), content);
    }

    public static <T> ResponseModel<T> success() {
        return new ResponseModel(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
    }

    public static <T> ResponseModel<T> fail(IErrorCode iErrorCode) {
        return new ResponseModel(iErrorCode.getCode(), iErrorCode.getMessage(), null);
    }

    public static <T> ResponseModel<T> fail() {
        return fail((IErrorCode) ResponseCode.FAIL);
    }

    public static <T> ResponseModel<T> fail(String message) {
        return new ResponseModel(ResponseCode.FAIL.getCode(), message, null);
    }
}
