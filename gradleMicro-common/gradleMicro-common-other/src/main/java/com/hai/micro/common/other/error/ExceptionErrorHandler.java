package com.hai.micro.common.other.error;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hai.micro.common.other.api.ResponseModel;
import com.hai.micro.common.other.constant.AuthConstants;
import com.hai.micro.common.other.request.RequestBodyContext;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ExceptionErrorHandler
 * @Description 全局异常处理
 * @Author ZXH
 * @Date 2021/12/9 19:15
 * @Version 1.0
 **/
@RestController
@ControllerAdvice
@Slf4j
public class ExceptionErrorHandler {

    /**
     * 业务异常
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    public ResponseModel businessExceptionHandler(HttpServletRequest request, RuntimeException e){
        IErrorCode errorCode = ((BusinessException) e).getErrorCode();
        String message = e.getMessage();
        if (errorCode == null) {
            return ResponseModel.fail(message);
        } else {
            return ResponseModel.fail(errorCode);
        }
    }

    /**
     * 运行时异常
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseModel runtimeExceptionHandler(HttpServletRequest request, RuntimeException e){
        getExceptionParam(request);
        log.error("运行时异常: ", e);
        if (Strings.isNotBlank(e.getMessage())) {
            return ResponseModel.fail(e.getMessage());
        } else {
            return ResponseModel.fail("空指针异常");
        }
    }

    /**
     * 其他异常
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseModel globalErrorHandler(HttpServletRequest request, Exception e) {
        getExceptionParam(request);
        log.error("其他异常: ", e);
        if (Strings.isNotBlank(e.getMessage())) {
            return ResponseModel.fail(e.getMessage());
        } else {
            return ResponseModel.fail("空指针异常");
        }
    }

    private void getExceptionParam(HttpServletRequest req) {
        StringBuilder error = new StringBuilder();
        error.append("\n--------------------------代码调试请求信息begin-----------------------------------------------");
        error.append("\n请求路径：" + req.getServletPath());
        error.append("\n会话Token：" + req.getHeader(AuthConstants.JWT_ACCESS_TOKEN));
        error.append("\n请求方式：" + req.getMethod());
        //error.append("\n请求IP：" + WebUtils.getIp(req));
        error.append("\nGet请求参数：" + JSONObject.toJSONString(req.getParameterMap()));
        if (RequestBodyContext.REQUEST_BODY.get() != null) {
            error.append("\nPost请求参数：" + RequestBodyContext.REQUEST_BODY.get().getBody());
        }
        error.append("\n--------------------------代码调试请求信息end-------------------------------------------------");
        log.error(error.toString());
    }
}
