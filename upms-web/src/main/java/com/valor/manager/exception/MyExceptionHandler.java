package com.valor.manager.exception;

import com.valor.model.exception.APIException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 */
@ControllerAdvice
public class MyExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Map<String, Object> errorHandler(Exception ex) {
        Map<String, Object> map = new HashMap<>();
        // 根据不同错误获取错误信息
        if (ex instanceof APIException) {
            map.put("code", ((APIException) ex).getRetCode());
            map.put("msg", ((APIException) ex).getMsg());
        } else if (ex instanceof AccessDeniedException) {
            map.put("code", 403);
            map.put("msg", "Access Denied");
        } else {
            String message = ex.getMessage();
            map.put("code", 500);
            map.put("msg", message == null || message.trim().isEmpty() ? "Busy Service" : message);
            map.put("details", message);
        }
        return map;
    }
}
