package com.st.mall.common.advice;


import com.st.mall.common.bean.RespBean;
import com.st.mall.common.exception.StException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

//全局异常处理
@RestControllerAdvice
public class WebExceptionHandler {
    @ExceptionHandler(StException.class)
    public RespBean exceptionHandler(StException e) {
        e.printStackTrace();
        return RespBean.error(e.getMessage());
    }
    @ExceptionHandler({BindException.class})
    public RespBean handleMethodArgumentNotValidException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sbf = new StringBuilder("校验失败 ");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sbf.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(", ");
        }
        String msg = sbf.toString();
        int i = msg.lastIndexOf(", ");
        msg = msg.substring(0, i);
        return RespBean.error(msg);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public RespBean handleConstraintViolationException(ConstraintViolationException e) {
        return RespBean.error(e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public RespBean exceptionHandler(Exception e) {
        e.printStackTrace();
        return RespBean.error("未知错误，请联系管理员");
    }
    @ExceptionHandler(RuntimeException.class)
    public RespBean runtimeexceptionHandler(RuntimeException e) {
        e.printStackTrace();
        return RespBean.error(e.getMessage());
    }

}
